package com.example.booksapp.Loader;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.booksapp.QueryUtils;
import com.example.booksapp.classes.Books;

import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<Books>> {
String mUrl;
    public BooksLoader(Context context,String url){
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
       forceLoad();
    }

    @Nullable
    @Override
    public List<Books> loadInBackground() {
       if(mUrl==null){
           return null;
       }
       List<Books>books= QueryUtils.fetchBooks(mUrl);
       return books;
    }
}
