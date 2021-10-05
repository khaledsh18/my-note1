package com.khaledshalabi.mynote.notesdetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.khaledshalabi.mynote.Constants;
import com.khaledshalabi.mynote.R;

public class ImageNoteActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText editText;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    private int pos;
    private Uri mPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_note);

        imageView = (ImageView) findViewById(R.id.photoImageView);
        relativeLayout = (RelativeLayout)findViewById(R.id.imageNoteRl);
        editText = (EditText) findViewById(R.id.photoNoteEditText);

        mPhotoUri = getIntent().getParcelableExtra(Constants.IMAGE);

        editText.setText(getIntent().getStringExtra(Constants.TEXT));
        relativeLayout.setBackgroundColor(getIntent().getIntExtra(Constants.COLOR,0));
        imageView.setImageURI(mPhotoUri);
        pos = getIntent().getIntExtra(Constants.POSITION,0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
    }

    @Override
    public void onBackPressed() {
        String text = editText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(Constants.TEXT,text);
        intent.putExtra(Constants.IMAGE, mPhotoUri);
        intent.putExtra(Constants.POSITION,pos);
        setResult(Constants.EDIT_IMAGE_NOTE,intent);
        finish();
        super.onBackPressed();
    }
    private void addImage(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent,"Choose photo"),PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null){
            setSelectedImage(data.getData());
            getContentResolver().takePersistableUriPermission(data.getData(),Intent.FLAG_GRANT_READ_URI_PERMISSION);

        }else{
            Toast.makeText(ImageNoteActivity.this,R.string.message_not_choose_image,Toast.LENGTH_SHORT).show();
        }
    }

    private void setSelectedImage(Uri data){
        imageView.setImageURI(data);
        mPhotoUri = data;
    }
}