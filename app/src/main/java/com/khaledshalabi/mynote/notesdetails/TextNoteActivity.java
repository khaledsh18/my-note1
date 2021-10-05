package com.khaledshalabi.mynote.notesdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.khaledshalabi.mynote.Constants;
import com.khaledshalabi.mynote.R;

public class TextNoteActivity extends AppCompatActivity {
    private static final String TAG = "TextNote";
    private ConstraintLayout constraintLayout;
    private EditText editText;
    private int color,pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        color = getIntent().getIntExtra(Constants.COLOR,0);

        constraintLayout = (ConstraintLayout)findViewById(R.id.textNoteCl);
        editText = (EditText) findViewById(R.id.noteEditText);
        constraintLayout.setBackgroundColor(color);
        editText.setText(getIntent().getStringExtra(Constants.TEXT));
        pos = getIntent().getIntExtra(Constants.POSITION,0);
    }

    @Override
    public void onBackPressed() {
        String text = editText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(Constants.TEXT,text);
        intent.putExtra(Constants.POSITION,pos);
        setResult(Constants.EDIT_TEXT_NOTE,intent);
        finish();
        super.onBackPressed();
    }
}