package com.example.booksapp.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.bumptech.glide.Glide;
import com.example.booksapp.R;
import com.example.booksapp.data.BookContract;

import org.w3c.dom.Text;

public class BookCursorAdapter  extends CursorAdapter {
    public BookCursorAdapter(Context context,Cursor c){
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
       return LayoutInflater.from(context).inflate(R.layout.saved_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title=(TextView)view.findViewById(R.id.saved_title);
        TextView author=(TextView)view.findViewById(R.id.saved_author);
        TextView publisher=(TextView)view.findViewById(R.id.saved_publisher);
        RatingBar mratingbar=(RatingBar)view.findViewById(R.id.saved_Rating_bar);
        ImageView imageView=(ImageView)view.findViewById(R.id.save_image_view);
        int nameColumnIndex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME);
        int authorcolumnindex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR);
        int imageViewcolumnindex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_IMAGE_LINK);
        int publishercolumnindex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PUBLISHER_NAME);
        int ratingcolumnindex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_RATING);
        String name=cursor.getString(nameColumnIndex);
        String authors=cursor.getString(authorcolumnindex);
        String imagelink=cursor.getString(imageViewcolumnindex);
        String publisherName=cursor.getString(publishercolumnindex);
        String rating=cursor.getString(ratingcolumnindex);
        title.setText(name);
        author.setText(authors);
        publisher.setText(publisherName);
        float r=Float.parseFloat(rating);
        mratingbar.setRating(r);
        Glide.with(view).load(imagelink).into(imageView);


    }
}
