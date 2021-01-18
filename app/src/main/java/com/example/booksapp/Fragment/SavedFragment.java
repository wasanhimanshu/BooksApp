package com.example.booksapp.Fragment;

import android.app.Person;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.booksapp.Adapter.BookCursorAdapter;
import com.example.booksapp.R;
import com.example.booksapp.classes.Books;
import com.example.booksapp.data.BookContract;

import java.util.List;
import java.util.Objects;

public class SavedFragment  extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public SavedFragment() {
    }
private BookCursorAdapter mAdapter;
    private TextView emptyTextiew;
    private static final int LOADER_ID=1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.saved_fragment, container, false);
        ListView listView=(ListView)rootView.findViewById(R.id.list);
        emptyTextiew=(TextView)rootView.findViewById(R.id.empty_text_View1);
        listView.setEmptyView(emptyTextiew);
       mAdapter=new BookCursorAdapter(getActivity(),null);
       listView.setAdapter(mAdapter);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.P)
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Uri uri=ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI,id);
          Cursor c=(Cursor)parent.getItemAtPosition(position);
          int Stringcolumnindex=c.getColumnIndex(BookContract.BookEntry.COLUMN_LINK);
          int authorColumnindex=c.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR);
          int titleColumnIndex=c.getColumnIndex(BookContract.BookEntry.COLUMN_NAME);

          String link=c.getString(Stringcolumnindex);
        String authors=c.getString(authorColumnindex);
        String title=c.getString(titleColumnIndex);

             DialogBoxFunction(uri,link,title,authors);
           }
       });
       Objects.requireNonNull(getActivity()).getSupportLoaderManager().initLoader(LOADER_ID,null,this);
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
    private void deleteAllBook(){
        Objects.requireNonNull(getActivity()).getContentResolver().delete(BookContract.BookEntry.CONTENT_URI,null,null);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projections={BookContract.BookEntry._ID,BookContract.BookEntry.COLUMN_NAME, BookContract.BookEntry.COLUMN_AUTHOR, BookContract.BookEntry.COLUMN_IMAGE_LINK,
                BookContract.BookEntry.COLUMN_LINK, BookContract.BookEntry.COLUMN_PUBLISHER_NAME, BookContract.BookEntry.COLUMN_RATING};

        return new CursorLoader(Objects.requireNonNull(getActivity()),BookContract.BookEntry.CONTENT_URI,projections,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
mAdapter.swapCursor(null);
    }

    private void DialogBoxFunction(Uri uri, String link, String title , String  authors){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.choose_an_action));
        String [] items={"Open Book","Delete Book","Share Details"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(link));
                      Objects.requireNonNull(getActivity()).startActivity(i);
                         break;
                    case 1:
                        int rowDeleted=getActivity().getContentResolver().delete(uri,null,null);
                        break;
                    case 2:
                        Intent shareintent=new Intent(Intent.ACTION_SEND);
                        shareintent.setType("text/plain");
                        shareintent.putExtra(Intent.EXTRA_TEXT,getString(R.string.amazing_book)+title + " By " + authors +"\n\n" +"Here's the Preview Link :\n"+link +"\n\n" +getString(R.string.do_read));
                        startActivity(Intent.createChooser(shareintent,"share Book"));
                }
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllBook();
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
}
