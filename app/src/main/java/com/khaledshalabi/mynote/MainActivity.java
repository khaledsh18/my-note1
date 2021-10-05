package com.khaledshalabi.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khaledshalabi.mynote.adapter.NotesAdapter;
import com.khaledshalabi.mynote.data.NewNote;
import com.khaledshalabi.mynote.listener.ItemClick;
import com.khaledshalabi.mynote.listener.ItemLongClick;
import com.khaledshalabi.mynote.notesdetails.CheckNoteActivity;
import com.khaledshalabi.mynote.notesdetails.ImageNoteActivity;
import com.khaledshalabi.mynote.notesdetails.TextNoteActivity;

import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainAct";
    private static final String PREF_NAME = "app_pref";
    private RecyclerView recyclerView;
    protected ArrayList<NewNote> noteList;
    private RecyclerView.LayoutManager mStaggeredGridLayoutManager;
    private RecyclerView.LayoutManager mGridLayoutManager;
    private FloatingActionButton addNew;
    protected NotesAdapter adapter;
    protected String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        noteList = new ArrayList<>();
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        adapter = new NotesAdapter(noteList, new ItemClick() {
                @Override
                public void onClick(int pos) {
                    openDetail(pos);
                }
            }, new ItemLongClick() {
                @Override
                public void onLongClick(int pos) {
                    deleteItem(pos);
                }
            });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        addNew = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddNote.class),Constants.ADD_NOTE);
            }
        });
        savedNotes();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_NOTE && data != null){
            String text = data.getStringExtra(Constants.TEXT);
            int color = data.getIntExtra(Constants.COLOR,R.color.white);

            if (resultCode == Constants.TEXT_NOTE) {
                NewNote newNote = new NewNote(text,color);
                addItem(newNote);
            }else if(resultCode == Constants.IMAGE_NOTE){
                Uri image = data.getParcelableExtra(Constants.IMAGE);
                NewNote newNote = new NewNote(text,image,color);
                addItem(newNote);
            } else if(resultCode == Constants.CHECK_NOTE){
                boolean check = data.getBooleanExtra(Constants.CHECK_BOX,false);
                NewNote newNote = new NewNote(text,check,color);
                addItem(newNote);
            }
        }
        if (requestCode == 321 && data != null){
            int pos = data.getIntExtra(Constants.POSITION,0);
            if (resultCode == Constants.EDIT_TEXT_NOTE){
                noteList.get(pos).setText(data.getStringExtra(Constants.TEXT));
                adapter.notifyItemChanged(pos);
            }
            if (resultCode == Constants.EDIT_CHECK_NOTE){
                noteList.get(pos).setText(data.getStringExtra(Constants.TEXT));
                noteList.get(pos).setCheckBox(data.getBooleanExtra(Constants.CHECK_BOX,false));
                adapter.notifyItemChanged(pos);
            }
            if (resultCode == Constants.EDIT_IMAGE_NOTE) {
                Uri image = data.getParcelableExtra(Constants.IMAGE);
                noteList.get(pos).setText(data.getStringExtra(Constants.TEXT));
                noteList.get(pos).setImage(image);
                adapter.notifyItemChanged(pos);
            }
        }
    }

    @Override
    protected void onStop() {
        saveView(noteList);
        super.onStop();
    }

    private void saveView(ArrayList<NewNote> noteList) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d(TAG, "saveView: note"+noteList);
        if (noteList.isEmpty()){
            editor.putInt("size", -1);
            editor.apply();
            return;
        }
        for (int i = 0; i<noteList.size();i++) {
            editor.putString("text" + i, noteList.get(i).getText());
            editor.putInt("color" + i, noteList.get(i).getColor());
            if (noteList.get(i).isTextNote())
                editor.putString("type" +i, "text");
            if (noteList.get(i).isImageNote()) {
                editor.putString("image" + i, noteList.get(i).getImage().toString());
                editor.putString("type" +i, "image");
            }
            if (noteList.get(i).isCheckNote())
                editor.putString("type" +i, "check");
                editor.putBoolean("check" + i, noteList.get(i).isChecked());
        }
        Log.d(TAG, "saveView: noteSize"+noteList.size());
        editor.putInt("size", noteList.size());
        editor.apply();
    }

    private void savedNotes(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);

        int x = sharedPreferences.getInt("size",-1);
        Log.d(TAG, "savedNotes: size "+x);
        if (x>-1) {
            for (int i = 0; i < x; i++) {
                String text = sharedPreferences.getString("text" + i, "");
                Log.d(TAG, "savedNotes: text " + i + ":" + text);
                int color = sharedPreferences.getInt("color" + i, 0);
                Log.d(TAG, "savedNotes: color " + i + ":" + color);
                String type = sharedPreferences.getString("type" + i, "");
                Log.d(TAG, "savedNotes: type " + i + ":" + type);
                if (type.equals("text")) {
                    NewNote newNote = new NewNote(text, color);
                    addItem(newNote);
                }
                if (type.equals("image")) {
                    Uri image = Uri.parse(sharedPreferences.getString("image" + i, ""));
                    Log.d(TAG, "savedNotes: image " + i + ":" + image);
                    NewNote newNote = new NewNote(text, image, color);
                    addItem(newNote);
                }
                if (type.equals("check")) {
                    boolean isChecked = sharedPreferences.getBoolean("check" + x, false);
                    NewNote newNote = new NewNote(text, isChecked, color);
                    addItem(newNote);
                }
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    private void addItem(NewNote newNote) {
        noteList.add(newNote);
        adapter.notifyItemInserted(noteList.size()-1);
    }

    private void openDetail(int pos){

        if (noteList.get(pos).isImageNote()){
            Intent intent = new Intent(MainActivity.this, ImageNoteActivity.class);
            intent.putExtra(Constants.IMAGE,noteList.get(pos).getImage());
            intent.putExtra(Constants.TEXT,noteList.get(pos).getText());
            intent.putExtra(Constants.COLOR,noteList.get(pos).getColor());
            intent.putExtra(Constants.POSITION,pos);
            startActivityForResult(intent,321);
        }
        if (noteList.get(pos).isCheckNote()){
            Intent intent = new Intent(MainActivity.this, CheckNoteActivity.class);
            intent.putExtra(Constants.CHECK_BOX,noteList.get(pos).isChecked());
            intent.putExtra(Constants.TEXT,noteList.get(pos).getText());
            intent.putExtra(Constants.COLOR,noteList.get(pos).getColor());
            intent.putExtra(Constants.POSITION,pos);
            startActivityForResult(intent,321);
        }
        if (noteList.get(pos).isTextNote()){
            Intent intent = new Intent(MainActivity.this, TextNoteActivity.class);
            intent.putExtra(Constants.TEXT,noteList.get(pos).getText());
            intent.putExtra(Constants.COLOR,noteList.get(pos).getColor());
            intent.putExtra(Constants.POSITION,pos);
            startActivityForResult(intent,321);
        }
    }

    protected void deleteItem( int pos){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("هل تريد حذف العنصر ؟")
                .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        noteList.remove(pos);
//                        removeFromSave(pos);
                        adapter.notifyItemRemoved(pos);
                        for (int i =pos;i<noteList.size();i++){
                            adapter.notifyItemChanged(i);
                        }
                    }
                }).setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        alertDialog.show();
    }
}