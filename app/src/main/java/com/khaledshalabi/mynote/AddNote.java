package com.khaledshalabi.mynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddNote extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final int PICK_IMAGE = 200;
    private static final int READ_PHOTO_FROM_GALLERY_PERMISSION = 210;
    private RadioGroup colors,types;
    private CardView choseCard;
    private int color;
    private Button submit;
    private RadioButton textNote,photoNote,checkNote;
    private Uri mPhotoUri;
    private String mNoteText;
    private boolean mChecked;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        imageView = (ImageView)findViewById(R.id.imageCardImageView);
        textNote = (RadioButton)findViewById(R.id.noteRadioButton);
        photoNote = (RadioButton)findViewById(R.id.imageRadioButton);
        checkNote = (RadioButton)findViewById(R.id.checkBoxRadioButton);

        submit = (Button)findViewById(R.id.addNoteButton);

        color = getResources().getColor(R.color.blue);
        choseCard = (CardView) findViewById(R.id.cardText);
        choseCard.setVisibility(View.VISIBLE);
        setCardColor(color);

        colors = (RadioGroup) findViewById(R.id.radioGroup);
        types = (RadioGroup) findViewById(R.id.radioGroup2);
        colors.setOnCheckedChangeListener(this);
        types.setOnCheckedChangeListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PHOTO_FROM_GALLERY_PERMISSION){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fireSelectPhotoIntent();
            }else {
                Toast.makeText(AddNote.this,R.string.permissionDenied,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null){
            setSelectedImage(data.getData());
            getContentResolver().takePersistableUriPermission(data.getData(),Intent.FLAG_GRANT_READ_URI_PERMISSION);

        }else{
            Toast.makeText(AddNote.this,R.string.message_not_choose_image,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.radioGroup){
                switch (checkedId){
                    case R.id.blueRadioButton:
                        setCardColor(getResources().getColor(R.color.blue));
                        break;
                    case R.id.redRadioButton:
                        setCardColor(getResources().getColor(R.color.red));
                        break;
                    case R.id.yellowRadioButton:
                        setCardColor(getResources().getColor(R.color.yellow));
                        break;
                }
        }
        if (group.getId() == R.id.radioGroup2){
            switch (checkedId){
                case R.id.noteRadioButton:
                    setCardView(R.id.cardText);
                    break;
                case R.id.imageRadioButton:
                    setCardView(R.id.cardImage);
                    break;
                case R.id.checkBoxRadioButton:
                    setCardView(R.id.cardCheckBox);
                    break;
            }
        }
    }

    private void setCardColor(int color){
        choseCard.setCardBackgroundColor(color);
        this.color = color;
    }
    private void setCardView(int id){
        if (choseCard.getId() != id){
            choseCard.setVisibility(View.GONE);
            choseCard = (CardView) findViewById(id);
            choseCard.setVisibility(View.VISIBLE);
            choseCard.setCardBackgroundColor(color);
        }
    }
    private void setText(){
        if (textNote.isChecked()){
            EditText editText = findViewById(R.id.noteCardEditText);
            mNoteText = editText.getText().toString();

        }else if (photoNote.isChecked()){
            EditText editText = findViewById(R.id.imageCardEditText);
            mNoteText = editText.getText().toString();

        }else if (checkNote.isChecked()){
            EditText editText = findViewById(R.id.checkNoteEditText);
            mNoteText = editText.getText().toString();
        }
    }

    private void setChecked(){
        CheckBox checkBox = findViewById(R.id.checkNoteCheckBox);
        if (checkNote.isChecked()){
            mChecked = checkBox.isChecked();
        }
    }

    private void addPhoto(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_PHOTO_FROM_GALLERY_PERMISSION);
        }else {
            fireSelectPhotoIntent();
        }
    }

    private void fireSelectPhotoIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent,"Choose photo"),PICK_IMAGE);
    }

    private void setSelectedImage(Uri data){
        imageView.setImageURI(data);
        mPhotoUri = data;
    }

    private void addNote(){
        setText();
        setChecked();
        if (textNote.isChecked()){
            // التحقق من ان القيم ليست فارغة
            if (!mNoteText.isEmpty()){
                Intent intent = new Intent();
                intent.putExtra(Constants.TEXT,mNoteText);
                intent.putExtra(Constants.COLOR,color);
                setResult(Constants.TEXT_NOTE,intent);
                finish();
            }else{
                Toast.makeText(this,R.string.null_var,Toast.LENGTH_SHORT).show();
            }
        }else if (photoNote.isChecked()){
            if (mPhotoUri != null && !mNoteText.isEmpty()) {
                // التحقق من ان القيم ليست فارغة
                Intent intent = new Intent();
                intent.putExtra(Constants.IMAGE, mPhotoUri);
                intent.putExtra(Constants.TEXT, mNoteText);
                intent.putExtra(Constants.COLOR,color);
                setResult(Constants.IMAGE_NOTE, intent);
                finish();
            }else{
                Toast.makeText(this,R.string.null_var,Toast.LENGTH_SHORT).show();
            }
        }else if (checkNote.isChecked()){
            if (!mNoteText.isEmpty()) {
                // التحقق من ان القيم ليست فارغة
                Intent intent = new Intent();
                intent.putExtra(Constants.TEXT, mNoteText);
                intent.putExtra(Constants.CHECK_BOX, mChecked);
                intent.putExtra(Constants.COLOR,color);
                setResult(Constants.CHECK_NOTE, intent);
                finish();
            }else{
                Toast.makeText(this,R.string.null_var,Toast.LENGTH_SHORT).show();
            }
        }
    }
}