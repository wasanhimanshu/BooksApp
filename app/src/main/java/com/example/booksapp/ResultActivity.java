package com.example.booksapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.booksapp.Adapter.BooksAdapter;
import com.example.booksapp.Loader.BooksLoader;
import com.example.booksapp.classes.Books;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<Books>> {
private RecyclerView recyclerView;
private TextView EmptyTextView;
ProgressBar mLoadingIndicator;
private BooksAdapter mAdapter;
private List<Books> books;
private static final int LOADER_ID=1;
private String BOOKS_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent i=getIntent();
        String Query=i.getExtras().getString("topicname");
        BOOKS_URL="https://www.googleapis.com/books/v1/volumes?q="+Query+"&maxResults=15";
        books=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EmptyTextView=(TextView)findViewById(R.id.empyt_text_view);
        mAdapter=new BooksAdapter(this,books);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.GONE);
        ConnectivityManager cr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cr.getActiveNetworkInfo();
        if(info!=null&&info.isConnected()){
getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }else{
            mLoadingIndicator=(ProgressBar)findViewById(R.id.loading_indicator);
            mLoadingIndicator.setVisibility(View.GONE);
            EmptyTextView.setText("No Internet Connection");
        }


    }

    @NonNull
    @Override
    public Loader<List<Books>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BooksLoader(this,BOOKS_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Books>> loader, List<Books> data) {
books.clear();
if(data!=null&&!data.isEmpty()){
    books.addAll(data);
    mAdapter.notifyDataSetChanged();
    EmptyTextView.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
}else{
    EmptyTextView.setText("No Books TO Show");
    EmptyTextView.setVisibility(View.VISIBLE);
}
mLoadingIndicator=(ProgressBar)findViewById(R.id.loading_indicator);
mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Books>> loader) {
books.clear();
mAdapter.notifyDataSetChanged();
    }
}