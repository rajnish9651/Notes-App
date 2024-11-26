package com.example.noteapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    List<NoteData> noteDataList;
    MainActivity context;
    ItemClick itemClick;

    public NoteAdapter(List<NoteData> noteDataList, MainActivity mainActivity, ItemClick itemClick) {
        this.noteDataList = noteDataList;
        this.context = mainActivity;//mainActivity context
        this.itemClick = itemClick;// itemclick context
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_items_recyler_view, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {

        NoteData noteData = noteDataList.get(position);
        holder.titleText.setText(noteData.getTitle());
        holder.contentText.setText(noteData.getContent());
        holder.textViewDate.setText(noteData.getDate());

        //update note when user click on note
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating and passing data  via intent to AddUpdateActivity
                Intent intent = new Intent(context, AddUpdateActivity.class);
                intent.putExtra("id", noteData.getId());
                intent.putExtra("title", noteData.getTitle());
                intent.putExtra("content", noteData.getContent());
                context.startActivity(intent);
            }
        });

        //delete note when user long press the note
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                showing pop-pu menu
                showPopupMenu(v, noteData, position);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteDataList.size();
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView contentText, titleText, textViewDate;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            contentText = itemView.findViewById(R.id.contentText);
            titleText = itemView.findViewById(R.id.titleText);
            textViewDate = itemView.findViewById(R.id.textViewDate);

        }

    }


    //searching note and refreshing the value
    public void filterList(List<NoteData> filterList) {
        noteDataList = filterList;
        notifyDataSetChanged();
    }


    // Show a popup menu with Delete, and Share options
    private void showPopupMenu(View view, NoteData noteData, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.note_item_menu, popupMenu.getMenu());  // Inflate or show custom menu

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_delete) {// for  delete action
                itemClick.delete(noteData.getId());
                noteDataList.remove(position);  // Remove the item from the list
//                notifyItemRemoved(position);   // Notify adapter about removal
                notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.menu_share) {// for share action
                shareNote(noteData);
                return true;
            }
            return false;
        });

        // Show the popup menu
        popupMenu.show();
    }


    // Share note method
    private void shareNote(NoteData noteData) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, noteData.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, noteData.getContent());
        context.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

}
