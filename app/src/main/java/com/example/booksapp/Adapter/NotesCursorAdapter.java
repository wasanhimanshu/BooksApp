package com.example.booksapp.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.booksapp.R;
import com.example.booksapp.data.BookContract;

import org.w3c.dom.Text;

public class NotesCursorAdapter extends CursorAdapter {
    public NotesCursorAdapter(Context context,Cursor c){
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item,parent,false);
     return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
TextView mTitleTextView=(TextView)view.findViewById(R.id.notes_list_item_title);
TextView mSummaryTextView=(TextView)view.findViewById(R.id.notes_list_item_summary);
int titlecolumnindex=cursor.getColumnIndex(BookContract.NotesEntry.COLUMN_TITLE);
int summaryColumnIndex=cursor.getColumnIndex(BookContract.NotesEntry.COLUMN_SUMMARY);
String title=cursor.getString(titlecolumnindex);
String summary=cursor.getString(summaryColumnIndex);
mTitleTextView.setText(title);
mSummaryTextView.setText(summary);
    }
}
