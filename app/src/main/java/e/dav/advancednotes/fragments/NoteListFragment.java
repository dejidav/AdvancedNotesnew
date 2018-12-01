package e.dav.advancednotes.fragments;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.GestureDetector;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import java.util.List;

import e.dav.advancednotes.Activities.NoteActivity;
import e.dav.advancednotes.R;

import e.dav.advancednotes.adapter.NoteListAdapter;
import e.dav.advancednotes.db.NoteManager;
import e.dav.advancednotes.model.Note;




public class NoteListFragment extends Fragment implements View.OnCreateContextMenuListener{

    private FloatingActionButton mFab;
    private View mRootView;
    private List<Note> mNotes;
    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;






    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment and hold the reference
        //in mRootView
        mRootView = inflater.inflate(R.layout.fragment_note_list, container, false);



        //Get a programmatic reference to the Floating Action Button
        mFab = mRootView.findViewById(R.id.fab);


        //attach an onClick listener to the Floating Action Button
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NoteActivity.class));

            }
        });

        registerForContextMenu(mRootView);




        setupList();
        return mRootView;


    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.context_edit:
                long idLong = mAdapter.getItemId(1);// issue
                int id = (int)idLong;
                Note nNote = NoteManager.newInstance(getActivity()).getNote(id);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NoteViewFragment NAME = new NoteViewFragment();
                Bundle b = new Bundle();
                b.putSerializable("Note", nNote);
                NAME.setArguments(b);
                fragmentTransaction.replace(R.id.container, NAME);
                fragmentTransaction.commit();

                Log.i("ContextMenu", "Item 1a was chosen");
                Toast.makeText(getActivity(), " edit Item clicked " , Toast.LENGTH_SHORT).show();
                return true;
            case R.id.context_share:
                Log.i("ContextMenu", "Item 1b was chosen");
                return true;
            case R.id.context_delete:
                Log.i("ContextMenu", "Item 1c was chosen");
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private void setupList() {
        mRecyclerView = mRootView.findViewById(R.id.note_recycler_view);
        mRecyclerView.setHasFixedSize(false);//true
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //execute AsyncTask here
        GetNotesTask task = new GetNotesTask();
        task.execute();




        final GestureDetector mGestureDetector =
                new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                    @Override// Gesture Listener
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildLayoutPosition(child);
                    Note selectedNote = mNotes.get((position));
                    Intent editorIntent = new Intent(getActivity(), NoteActivity.class);
                    editorIntent.putExtra("id", selectedNote.getId());
                    startActivity(editorIntent);



                //    Toast.makeText(getActivity(), " edit Item clicked "+ selectedNote.getId() , Toast.LENGTH_SHORT).show();

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });




    }
    //AsyncTask
    public class GetNotesTask extends AsyncTask<Void, Void, NoteListAdapter> {


        @Override
        protected NoteListAdapter doInBackground(Void... voids) {
            mNotes = NoteManager.newInstance(getActivity()).getAllNotes();
            mAdapter = new NoteListAdapter(mNotes, getActivity());
            return mAdapter;
        }

        @Override
        protected void onPostExecute(NoteListAdapter noteListAdapter) {
           // super.onPostExecute(noteListAdapter);
            mRecyclerView.setAdapter(noteListAdapter);
        }


    }



}
