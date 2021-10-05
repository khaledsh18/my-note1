package com.khaledshalabi.mynote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.khaledshalabi.mynote.listener.ItemClick;
import com.khaledshalabi.mynote.listener.ItemLongClick;
import com.khaledshalabi.mynote.data.NewNote;
import com.khaledshalabi.mynote.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter {

    private List<NewNote> noteList;
    private static final int NOTE_TEXT_CODE = 40;
    private static final int NOTE_IMAGE_CODE = 41;
    private static final int NOTE_CHECKBOX_CODE = 42;
    private ItemLongClick mItemLongClick;
    private ItemClick mItemClick;


    public NotesAdapter(List<NewNote> noteList,ItemClick itemClick , ItemLongClick itemLongClick){
        this.noteList = noteList;
        this.mItemClick = itemClick;
        this.mItemLongClick = itemLongClick;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType == NOTE_TEXT_CODE){
            View view = inflater.inflate(R.layout.note_card,parent,false);
            NotesViewHolder notesViewHolder = new NotesViewHolder(view,mItemClick,mItemLongClick);
            return notesViewHolder;

        }else if (viewType == NOTE_CHECKBOX_CODE){

            View view = inflater.inflate(R.layout.check_note_card,parent,false);
            NotesCheckBoxViewHolder notesCheckBoxViewHolder  = new NotesCheckBoxViewHolder(view,mItemClick,mItemLongClick);
            return notesCheckBoxViewHolder;

        }else {

            View view = inflater.inflate(R.layout.image_note_card,parent,false);
            NotesPhotoViewHolder notesPhotoViewHolder = new NotesPhotoViewHolder(view,mItemClick,mItemLongClick);
            return notesPhotoViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(noteList.get(position).isImageNote()){
            NotesPhotoViewHolder notesPhotoViewHolder = (NotesPhotoViewHolder) holder;
            notesPhotoViewHolder.image.setImageURI(noteList.get(position).getImage());
            notesPhotoViewHolder.text.setText(noteList.get(position).getText());
            notesPhotoViewHolder.cardView.setCardBackgroundColor(noteList.get(position).getColor());
            notesPhotoViewHolder.position = holder.getAdapterPosition();

        }else if (noteList.get(position).isCheckNote()){
            NotesCheckBoxViewHolder notesCheckBoxViewHolder = (NotesCheckBoxViewHolder) holder;
            notesCheckBoxViewHolder.checkBox.setChecked(noteList.get(position).isChecked());
            notesCheckBoxViewHolder.text.setText(noteList.get(position).getText());
            notesCheckBoxViewHolder.cardView.setCardBackgroundColor(noteList.get(position).getColor());
            notesCheckBoxViewHolder.position = holder.getAdapterPosition();

        }else{
            NotesViewHolder notesViewHolder =(NotesViewHolder) holder;
            notesViewHolder.text.setText(noteList.get(position).getText());
            notesViewHolder.cardView.setCardBackgroundColor(noteList.get(position).getColor());
            notesViewHolder.position = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(noteList.get(position).isImageNote()){
            return NOTE_IMAGE_CODE;
        }else if (noteList.get(position).isCheckNote()){
            return NOTE_CHECKBOX_CODE;
        }else{
            return NOTE_TEXT_CODE;
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView text;
        private int position;
        public NotesViewHolder(@NonNull View itemView, final ItemClick mItemClick, final ItemLongClick mItemLongClick) {
            super(itemView);
            text = itemView.findViewById(R.id.noteTextView);
            cardView = itemView.findViewById(R.id.noteCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onClick(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemLongClick.onLongClick(position);
                    return true;
                }
            });
        }
    }

    static class NotesCheckBoxViewHolder extends RecyclerView.ViewHolder{

        private CheckBox checkBox;
        private TextView text;
        private int position;
        private CardView cardView;

        public NotesCheckBoxViewHolder(@NonNull View itemView, final ItemClick mItemClick, final ItemLongClick mItemLongClick) {
            super(itemView);
            text = itemView.findViewById(R.id.checkNoteTextView);
            checkBox = itemView.findViewById(R.id.checkNoteCheckBox);
            cardView = itemView.findViewById(R.id.checkNoteCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onClick(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemLongClick.onLongClick(position);
                    return true;
                }
            });
        }
    }

    static class NotesPhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView text;
        private int position;
        private CardView cardView;

        public NotesPhotoViewHolder(@NonNull View itemView, final ItemClick mItemClick, final ItemLongClick mItemLongClick) {
            super(itemView);
            image = itemView.findViewById(R.id.photoNoteImage);
            text = itemView.findViewById(R.id.imageNoteText);
            cardView = itemView.findViewById(R.id.imageNoteCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClick.onClick(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemLongClick.onLongClick(position);
                    return true;
                }
            });
        }
    }
}
