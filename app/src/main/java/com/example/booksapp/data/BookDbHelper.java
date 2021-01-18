package com.example.booksapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BookDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="books.db";
    private static final int DATABASE_VERSION=1;
    public BookDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_ENTRIES=" CREATE TABLE "+ BookContract.BookEntry.TABLE_NAME+ "(" +
                BookContract.BookEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                BookContract.BookEntry.COLUMN_NAME +" TEXT NOT NULL, "+
                BookContract.BookEntry.COLUMN_AUTHOR +" TEXT NOT NULL, "+
                BookContract.BookEntry.COLUMN_LINK + " TEXT NOT NULL, "+
                BookContract.BookEntry.COLUMN_IMAGE_LINK +" TEXT NOT NULL, "+
                BookContract.BookEntry.COLUMN_PUBLISHER_NAME +" TEXT NOT NULL, "+
                BookContract.BookEntry.COLUMN_RATING +" TEXT NOT NULL);";

        String NOTES_SQL_ENTRIES=" CREATE TABLE "+BookContract.NotesEntry.TABLE_NAME+"("+
                BookContract.NotesEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BookContract.NotesEntry.COLUMN_TITLE +" TEXT NOT NULL, "+
                BookContract.NotesEntry.COLUMN_SUMMARY+" TEXT NOT NULL);";


        db.execSQL(SQL_ENTRIES);
        db.execSQL(NOTES_SQL_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
