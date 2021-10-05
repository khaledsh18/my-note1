package com.khaledshalabi.mynote.data;

import android.net.Uri;

public class NewNote {
    private Uri image;
    private String text;
    private boolean checkBox;
    private boolean isThereCheckBox;
    private boolean isThereImage;
    private boolean isThereText;
    private int color;


    public NewNote(String text,int color) {
        this.text = text;
        this.color = color;
        this.isThereText = true;
    }

    public NewNote(String text, Uri image, int color) {
        this.image = image;
        this.isThereImage = true;
        this.text = text;
        this.color = color;
    }

    public NewNote(String text, boolean checkBox, int color) {
        this.text = text;
        this.checkBox = checkBox;
        this.isThereCheckBox = true;
        this.color = color;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image){
        this.image = image;
    }
    public String getText() {
        return text;
    }

    public boolean isChecked() {
        return checkBox;
    }

    public boolean isImageNote() {
        return isThereImage;
    }

    public boolean isCheckNote(){
        return isThereCheckBox;
    }

    public boolean isTextNote(){
        return isThereText;
    }

    public int getColor(){
        return color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
