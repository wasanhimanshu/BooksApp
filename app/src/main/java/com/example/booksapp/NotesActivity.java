package com.example.booksapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booksapp.Adapter.NotesCursorAdapter;
import com.example.booksapp.Adapter.TopicsAdapter;
import com.example.booksapp.Fragment.NotesFragment;
import com.example.booksapp.data.BookContract;
import com.example.booksapp.data.BookDbHelper;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import javax.microedition.khronos.egl.EGLDisplay;

public class NotesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
private EditText mSumaryEditText;
private EditText mTitleEditText;
 private  Uri mCurrentUri;
 private static int LOADER_ID=1;
 private NotesCursorAdapter mAdapter;
 private boolean mNotesHasChanged=false;
 private boolean isFilled=true;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mNotesHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i=getIntent();
        mCurrentUri=i.getData();
        if(mCurrentUri==null){
            setTitle(getString(R.string.ad_notes));
        }else{
            setTitle(getString(R.string.edit_note));
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }
        mTitleEditText=(EditText) findViewById(R.id.notes_title_edit_text);
        mSumaryEditText=(EditText) findViewById(R.id.notes_summary_edit_text);
        mAdapter=new NotesCursorAdapter(this,null);

        mTitleEditText.setOnTouchListener(mTouchListener);
        mSumaryEditText.setOnTouchListener(mTouchListener);
    }

    private void addNotes(){
        String title=mTitleEditText.getText().toString().trim();
        String summary=mSumaryEditText.getText().toString().trim();
        if(mCurrentUri==null&&TextUtils.isEmpty(title)&&TextUtils.isEmpty(summary)){
            return;
        }

        ContentValues values=new ContentValues();
        values.put(BookContract.NotesEntry.COLUMN_TITLE,title);
        values.put(BookContract.NotesEntry.COLUMN_SUMMARY,summary);
        if(mCurrentUri==null) {
            Uri newUri = getContentResolver().insert(BookContract.NotesEntry.CONTENT_URI_NOTES, values);
            if (newUri == null) {
                Toast.makeText(this, "Error Saving Notes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notes Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        }else{
            int rowUpdated=getContentResolver().update(mCurrentUri,values,null,null);
            if(rowUpdated==0){
                Toast.makeText(this,getString(R.string.Notes_not_Updated),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,getString(R.string.Notes_Updated),Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                if(isfine()) {
                    addNotes();
                    finish();
                    return true;
                }else{
                    break;
                }
                case android.R.id.home:
                if(!mNotesHasChanged) {
                    NavUtils.navigateUpFromSameTask(NotesActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
NavUtils.navigateUpFromSameTask(NotesActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String [] projections={BookContract.NotesEntry._ID,BookContract.NotesEntry.COLUMN_TITLE,BookContract.NotesEntry.COLUMN_SUMMARY};
        return new CursorLoader(this,mCurrentUri,projections,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
if(cursor==null||cursor.getCount()<1){
    return;
}
if(cursor.moveToNext()){
    int titleColumnIndex=cursor.getColumnIndex(BookContract.NotesEntry.COLUMN_TITLE);
    int summaryColumnIndex=cursor.getColumnIndex(BookContract.NotesEntry.COLUMN_SUMMARY);
    String title=cursor.getString(titleColumnIndex);
    String summary=cursor.getString(summaryColumnIndex);
    mTitleEditText.setText(title);
    mSumaryEditText.setText(summary);
}
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
mSumaryEditText.setText("");
mTitleEditText.setText("");
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
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
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mNotesHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private boolean isfine(){
        String title=mTitleEditText.getText().toString().trim();
        String summary=mSumaryEditText.getText().toString().trim();
        if(mCurrentUri==null&&TextUtils.isEmpty(title)&&TextUtils.isEmpty(summary)){
            isFilled=false;
            return isFilled;
        }
        if(isFilled&& TextUtils.isEmpty(title)){
            mTitleEditText.setError("Please fill the Title Appropriately");
            return false;
        }
        if(isFilled&&TextUtils.isEmpty(summary)){
            mSumaryEditText.setError("Please fill the Summary Appropriately");
            return false;
        }
        return true;
    }
}