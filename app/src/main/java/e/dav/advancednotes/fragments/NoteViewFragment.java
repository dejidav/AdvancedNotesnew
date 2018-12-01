package e.dav.advancednotes.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import e.dav.advancednotes.MainActivity;
import e.dav.advancednotes.R;
import e.dav.advancednotes.db.NoteManager;
import e.dav.advancednotes.model.Note;

public class NoteViewFragment extends Fragment {
    private View mRootView;
    private Note mNote;
    private TextView nTitle;
    private TextView nContent;
    private ImageView nAttachment;
    private TextView nCreationDate;


    public NoteViewFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    public static NoteViewFragment newInstance(int id){
        NoteViewFragment fragment = new NoteViewFragment();
        if (id > 0){
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            fragment.setArguments(bundle);


        }
        return fragment;
    }

    private void getCurrentNote(){
        Bundle args = getArguments();
        if (args != null && args.containsKey("id")){
            int id = args.getInt("id", 0);
            if (id > 0){
                mNote = NoteManager.newInstance(getActivity()).getNote(id);
                //   makeToast("current note view " + mCurrentNote.getId()+mCurrentNote.getTitle());
            }

        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_view_note, container, false);

        nTitle = mRootView.findViewById(R.id.note_title_view);
        nContent = mRootView.findViewById(R.id.note_content_view);
        nAttachment = mRootView.findViewById(R.id.imageView);
        nCreationDate = mRootView.findViewById(R.id.date_view);

        getCurrentNote();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNote != null){
            setUpNote();
        }
    }

    private void setUpNote(){
        nTitle.setText(mNote.getTitle());
        nContent.setText(mNote.getContent());
        nCreationDate.setText(mNote.getReadableCreatedDate());
        if(mNote.getAttachment()!= null) {
            File img = new File(mNote.getAttachment());
            Log.i("img path", "setUpNote: "+ mNote.getAttachment());

                Bitmap myBitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
                nAttachment.setImageBitmap(myBitmap);

        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_note_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_edit:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NoteLinedEditorFragment fragment = new NoteLinedEditorFragment();
                Bundle b = new Bundle();
                b.putSerializable("id", mNote.getId());
                fragment.setArguments(b);
                fragmentTransaction.replace(R.id.container2, fragment);
                fragmentTransaction.commit();
                return true;

            case R.id.menu_delete:

                if (mNote != null){
                    promptForDelete();
                }else {
                    makeToast("Cannot delete note that has not been saved");
                }
                return true;

            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void promptForDelete(){
        final String titleOfNoteTobeDeleted = mNote.getTitle();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Delete " + titleOfNoteTobeDeleted + " ?");
        alertDialog.setMessage("Are you sure you want to delete the note " + titleOfNoteTobeDeleted + "?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NoteManager.newInstance(getActivity()).delete(mNote);
                makeToast(titleOfNoteTobeDeleted + "deleted");
                startActivity(new Intent(getActivity(), MainActivity.class));
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
