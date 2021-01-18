package com.example.booksapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int BOOKS = 100;
    private static final int BOOKS_ID = 101;
    private static final int NOTES=102;
    private static final int NOTES_ID=103;

    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOKS_ID);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH_NOTES,NOTES);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH_NOTES+"/#",NOTES_ID);
    }

    private BookDbHelper mDbhelper;

    @Override
    public boolean onCreate() {
        mDbhelper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbhelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case BOOKS:
                cursor = db.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            case BOOKS_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            case NOTES:
                cursor=db.query(BookContract.NotesEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;
            case NOTES_ID:
                selection=BookContract.NotesEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(BookContract.NotesEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("Query is Not Supported for " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            case NOTES:
                return BookContract.NotesEntry.CONTENT_LIST_TYPE;
            case NOTES_ID:
                return BookContract.NotesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + " with Match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, values);
            case NOTES:
                return insertNotes(uri,values);
            default:
                throw new IllegalArgumentException("Insert is Not Supported For " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int rowDeleted;
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        switch (match) {
            case BOOKS:
                rowDeleted = db.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOKS_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = db.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES:
                rowDeleted=db.delete(BookContract.NotesEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case NOTES_ID:
                selection=BookContract.NotesEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted=db.delete(BookContract.NotesEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is Not Supported For " + uri);
        }
        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
       final int match=sUriMatcher.match(uri);
       switch (match){
           case NOTES:
               return updateNotes(uri,values,selection,selectionArgs);
           case NOTES_ID:
               selection=BookContract.NotesEntry._ID+"=?";
               selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
               return updateNotes(uri,values,selection,selectionArgs);
           default:
               throw new IllegalArgumentException("Update is Not for Suppoted for "+uri);
       }
    }

    private Uri insertBook(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        long id = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e("BookProvider", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }
    private Uri insertNotes(Uri uri,ContentValues values){
      String title=values.getAsString(BookContract.NotesEntry.COLUMN_TITLE);
      if(title==null){
          throw new IllegalArgumentException("Please Enter a valid Book Name");
      }
      String summary=values.getAsString(BookContract.NotesEntry.COLUMN_SUMMARY);
      if(summary==null){
          throw new IllegalArgumentException("Please Enter a valid Summary ");
      }
      SQLiteDatabase db=mDbhelper.getWritableDatabase();
      long newRowid=db.insert(BookContract.NotesEntry.TABLE_NAME,null,values);
      if(newRowid==-1){
          Log.e("BookProvider", "Failed to insert row for " + uri);
      }else{

      }
        getContext().getContentResolver().notifyChange(uri, null);
      return ContentUris.withAppendedId(uri,newRowid);
    }

private int updateNotes(Uri uri,ContentValues values,String selection,String[]selectionArgs){
        if(values.containsKey(BookContract.NotesEntry.COLUMN_TITLE)){
            String title=values.getAsString(BookContract.NotesEntry.COLUMN_TITLE);
            if(title==null){
                throw new IllegalArgumentException("Please Enter a valid Book Name");
            }
        }
        if(values.containsKey(BookContract.NotesEntry.COLUMN_SUMMARY)){
            String summary=values.getAsString(BookContract.NotesEntry.COLUMN_SUMMARY);
            if(summary==null){
                throw new IllegalArgumentException("Please Enter a valid Summary ");
            }
        }
        SQLiteDatabase db=mDbhelper.getWritableDatabase();
        int rowUpdates=db.update(BookContract.NotesEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowUpdates!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowUpdates;
}

}