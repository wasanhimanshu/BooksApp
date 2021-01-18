package com.example.booksapp.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.google.android.material.internal.ContextUtils;

public class BookContract {
    private BookContract(){}
public static final String CONTENT_AUTHORITY="com.example.booksapp";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS="books";
    public static final class BookEntry implements BaseColumns{
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_BOOKS);
        public static  final String TABLE_NAME="books";
        public static final String _ID=BaseColumns._ID;
        public static  final String COLUMN_NAME="name";
        public static final String COLUMN_AUTHOR="author_name";
        public static final String COLUMN_LINK="link";
        public static final String COLUMN_PUBLISHER_NAME="publisher_name";
        public static final String COLUMN_RATING="rating";
        public static final String COLUMN_IMAGE_LINK="image_link";
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;


    }
    public static final String PATH_NOTES="notes";
    public static  final class NotesEntry implements BaseColumns{
        public static final Uri CONTENT_URI_NOTES=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NOTES);
        public static final String TABLE_NAME="notes";
        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_SUMMARY="summary";
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

    }
}
