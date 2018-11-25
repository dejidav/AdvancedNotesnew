package e.dav.advancednotes.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import e.dav.advancednotes.R;
import e.dav.advancednotes.fragments.NoteLinedEditorFragment;
import e.dav.advancednotes.model.Note;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>{

    private List<Note> mNotes;
    private Context mContext;




    //constructor
    public NoteListAdapter(List<Note> notes, Context context){
        mNotes = notes;
        mContext = context;
        ;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }


    //HERE you bind one item of your list to the view holder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Note mnote = mNotes.get((position));
        holder.noteTitle.setText(mNotes.get((position)).getTitle());
        holder.noteCreateDate.setText(mNotes.get((position)).getReadableModifiedDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemClickListener itemClickListener = new ItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView.ViewHolder vh, Object item, int pos) {
                        this.onItemClicked(holder, mnote, position);
                    }
                };

            }
        });


        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


                contextMenu.add(Menu.NONE, R.id.context_edit,0,"edit");
                contextMenu.add(Menu.NONE, R.id.context_share,0,"share");
                contextMenu.add(Menu.NONE, R.id.context_delete, 0, "Delete");

            }




        });





    }





    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView noteTitle, noteCreateDate;



        public ViewHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.text_view_note_title);
            noteCreateDate = itemView.findViewById(R.id.text_view_note_date);
        }



    }


    public void promptForDelete(final int position){
        String fieldToBeDeleted = mNotes.get(position).getTitle();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Delete " + fieldToBeDeleted + " ?");
        alertDialog.setMessage("Are you sure you want to delete the note " + fieldToBeDeleted + "?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNotes.remove(position);
                notifyItemRemoved(position);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }




}
