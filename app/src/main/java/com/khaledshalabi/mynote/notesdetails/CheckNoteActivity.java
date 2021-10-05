package com.khaledshalabi.mynote.notesdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.khaledshalabi.mynote.Constants;
import com.khaledshalabi.mynote.R;

public class CheckNoteActivity extends AppCompatActivity {

    private EditText editText;
    private CheckBox checkBox;
    private ConstraintLayout constraintLayout;
    private int pos;
    private boolean isChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_check);

        constraintLayout = (ConstraintLayout)findViewById(R.id.checkNoteCl);
        editText = (EditText) findViewById(R.id.checkNoteEditText);
        checkBox = (CheckBox) findViewById(R.id.checkNoteCheckBox);

        pos = getIntent().getIntExtra(Constants.POSITION,0);
        isChecked = getIntent().getBooleanExtra(Constants.CHECK_BOX,false);

        editText.setText(getIntent().getStringExtra(Constants.TEXT));
        checkBox.setChecked(isChecked);
        constraintLayout.setBackgroundColor(getIntent().getIntExtra(Constants.COLOR,0));


    }

    @Override
    public void onBackPressed() {
        String text = editText.getText().toString();
        isChecked = checkBox.isChecked();
        Intent intent = new Intent();
        intent.putExtra(Constants.TEXT,text);
        intent.putExtra(Constants.CHECK_BOX,isChecked);
        intent.putExtra(Constants.POSITION,pos);
        setResult(Constants.EDIT_CHECK_NOTE,intent);
        finish();
        super.onBackPressed();
    }
}