package com.example.booksapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksapp.Adapter.BooksAdapter;
import com.example.booksapp.Loader.BooksLoader;
import com.example.booksapp.R;
import com.example.booksapp.classes.Books;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Books>> {
    private RecyclerView recyclerView;
    private TextView emptyTextView;
    ProgressBar mLoadingIndicator;
    private List<Books>books;
    private BooksAdapter mAdapter;
    private  EditText mEditText;
    private ImageButton mImageButton;
    private View rootView;
    private String BOOKS_URL_1;
    private static final int LOADER_ID=1;
    private int check=0,n=0;
    private String prevSearch;
    public SearchFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       rootView =inflater.inflate(R.layout.search_fragment,container,false);
      books=new ArrayList<>();
      recyclerView=(RecyclerView)rootView.findViewById(R.id.search_recycler_view);
      recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      emptyTextView=(TextView)rootView.findViewById(R.id.search_empty_view);
        mImageButton=(ImageButton)rootView.findViewById(R.id.search_image_view);

      mAdapter=new BooksAdapter(getActivity(),books);
      recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
   mEditText=(EditText)rootView.findViewById(R.id.search_edit_text);
mLoadingIndicator=(ProgressBar) rootView.findViewById(R.id.search_loading_indicator);
mLoadingIndicator.setVisibility(View.GONE);

        ConnectivityManager cr=(ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cr.getActiveNetworkInfo();
        prevSearch="";
        if(info!=null&&info.isConnected()){
         mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    InputMethodManager inputManager = (InputMethodManager)
                            Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    String search = mEditText.getText().toString().trim();
                    if(!prevSearch.equals(search)&n>0){
                        check=1;
                    }
                  if(search.isEmpty()){
                      mEditText.setError("please Enter a valid Book name");
                      mEditText.requestFocus();
                      return;
                   }
                  searchFunction(search);
                }
            });

        }else{
            emptyTextView.setText("No Internet Connection");
            emptyTextView.setVisibility(View.VISIBLE);
        }

     return rootView;
    }

    private void searchFunction(String search){
        emptyTextView.setText("");
        View LoadingIndicator=rootView.findViewById(R.id.search_loading_indicator);
        LoadingIndicator.setVisibility(View.VISIBLE);
        BOOKS_URL_1="https://www.googleapis.com/books/v1/volumes?q=" +search + "&maxResults=15";
        if(check==1){
            getLoaderManager().restartLoader(LOADER_ID,null,this);
            prevSearch=search;
        }else{
getLoaderManager().initLoader(LOADER_ID,null,this);
        }
n++;

    }

    @NonNull
    @Override
    public Loader<List<Books>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BooksLoader(getActivity(),BOOKS_URL_1);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Books>> loader, List<Books> data) {
        books.clear();
        if (data != null && !data.isEmpty()) {
            books.addAll(data);
            mAdapter.notifyDataSetChanged();
            emptyTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            emptyTextView.setText("No Books TO Show");
            emptyTextView.setVisibility(View.VISIBLE);
        }
        mLoadingIndicator=(ProgressBar) rootView.findViewById(R.id.search_loading_indicator);
        mLoadingIndicator.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Books>> loader) {
books.clear();
mAdapter.notifyDataSetChanged();
    }
}
