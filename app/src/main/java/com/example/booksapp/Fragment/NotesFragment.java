package com.example.booksapp.Fragment;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.booksapp.Adapter.NotesCursorAdapter;
import com.example.booksapp.NotesActivity;
import com.example.booksapp.R;
import com.example.booksapp.data.BookContract;
import com.example.booksapp.data.BookDbHelper;
import com.example.booksapp.data.BookProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    View rootView;
    private static final int LOADER_ID=0;
    private NotesCursorAdapter mAdapter;
 private   TextView mEmptyTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       rootView= inflater.inflate(R.layout.notes_fragment,container,false);

        FloatingActionButton fab=(FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent (getContext(), NotesActivity.class);
               Objects.requireNonNull(getContext()).startActivity(i);
            }
        });
        ListView listView=(ListView)rootView.findViewById(R.id.notes_list_item);
        mEmptyTextView=(TextView)rootView.findViewById(R.id.notes_empty_text_view);
        mAdapter=new NotesCursorAdapter(getContext(),null);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(mEmptyTextView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               setUpDialogBox(id);
            }
        });
       getActivity().getSupportLoaderManager().initLoader(LOADER_ID,null,this);

       return rootView;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.saved_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                showDeleteConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteAllNotes(){
        Objects.requireNonNull(getActivity()).getContentResolver().delete(BookContract.NotesEntry.CONTENT_URI_NOTES,null,null);
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_dialog_msg1);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllNotes();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String [] projections={BookContract.NotesEntry._ID,BookContract.NotesEntry.COLUMN_TITLE,BookContract.NotesEntry.COLUMN_SUMMARY};
        return new CursorLoader(getActivity(),BookContract.NotesEntry.CONTENT_URI_NOTES,projections,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
mAdapter.swapCursor(null);
    }

    private void setUpDialogBox(long id){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle(getActivity().getString(R.string.choose_an_action));
        String [] items={"Edit","Delete"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent i=new Intent(getActivity(),NotesActivity.class);
                        Uri mCurrentUri= ContentUris.withAppendedId(BookContract.NotesEntry.CONTENT_URI_NOTES,id);
                        i.setData(mCurrentUri);
                        getActivity().startActivity(i);
                        break;
                    case 1:
                        int rowDeleted=getContext().getContentResolver().delete(BookContract.NotesEntry.CONTENT_URI_NOTES,null,null);


                }
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }
}
