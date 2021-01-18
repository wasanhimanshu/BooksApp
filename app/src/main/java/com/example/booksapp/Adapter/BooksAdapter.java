package com.example.booksapp.Adapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booksapp.R;
import com.example.booksapp.classes.Books;
import com.example.booksapp.data.BookContract;
import com.example.booksapp.data.BookProvider;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {
private List<Books>mBooks;
private Context mContext;
private boolean mExist=false;
public BooksAdapter(Context context, List<Books> books){
    mBooks=books;
    mContext=context;
}

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
     return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
final Books books=mBooks.get(position);
holder.title.setText(books.getmTitle());
if(books.getmAuthor()!=null){
    StringBuilder builder=new StringBuilder();
    for(String names:books.getmAuthor()){
     builder.append(names+ " , ");
    }
    String str=builder.toString();
    str=str.substring(0,str.length()-2);
    holder.authors.setText(str);
}else{
    holder.authors.setText("No author Provided");
}
holder.publisher.setText(books.getmPublisher());
if(books.getmDescription()!=null){
    holder.description.setText(books.getmDescription());
}else{
    holder.description.setText("No Description Provided");

}
if(books.getmPublishedDate()!=null){
            holder.dateTextView.setText(books.getmPublishedDate());
        }else{
            holder.dateTextView.setVisibility(View.GONE);
        }

       if(books.getmRating()==null){
           float val=3;
           holder.mratingBar.setRating(val);
       }else{
           String ans=books.getmRating();
           float val=Float.parseFloat(ans);
           holder.mratingBar.setRating(val);
       }


if(books.getmImageLink()!=null){
    Glide.with(mContext.getApplicationContext())
            .load(books.getmImageLink())
            .into(holder.imageView);
}

holder.cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

       cardViewDialogBox(books.getmLink(),books.getmAuthor(),books.getmTitle(),books.getmImageLink(),books.getmPublisher(),books.getmRating());
    }
});

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public static  class BooksViewHolder extends RecyclerView.ViewHolder{
TextView title;
TextView description;
TextView authors;
TextView publisher;
ImageView imageView;
TextView dateTextView;
RatingBar mratingBar;
View rootView;
CardView cardView;
        public BooksViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView=itemView;
            title=(TextView)rootView.findViewById(R.id.title_text_view);
            authors=(TextView)rootView.findViewById(R.id.authors_text_view);
            description=(TextView)rootView.findViewById(R.id.description_text_view);
            publisher=(TextView)rootView.findViewById(R.id.publisher_text_view);
            imageView=(ImageView)rootView.findViewById(R.id.image_view);
            dateTextView=(TextView)rootView.findViewById(R.id.date_text_view);
            mratingBar=(RatingBar)rootView.findViewById(R.id.rating_bar);
          cardView=(CardView)rootView.findViewById(R.id.card_view);
        }
    }
    private void cardViewDialogBox(String link,List<String> authors,String title,String imageLink,String publisher,String rating){
     AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.choose_an_action));
        String [] items={"Open Book","Save Book","Share Details"};

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(link));
                        mContext.startActivity(i);
                        break;
                    case 1:
                        if(checkIfBookAlreadyExist(title)){
                            Toast.makeText(mContext,"Book Already Saved",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        ContentValues values=new ContentValues();
                        StringBuilder builder=new StringBuilder();
                        String str="";
                        if(authors==null){
                          str="No Author Provided";
                        }else {
                            for (String names : authors) {
                                builder.append(names + " , ");
                            }

                            str=builder.toString();
                            str=str.substring(0,str.length()-2);
                        }

                    String publishers;
                        if(publisher==null){
                            publishers="No Publisher Provided";
                        }else{
                            publishers=publisher;
                        }
                        String ratings;
                        if(rating==null){
                            ratings="3";
                        }else{
                            ratings=rating;
                        }
                        values.put(BookContract.BookEntry.COLUMN_NAME,title);
                        values.put(BookContract.BookEntry.COLUMN_AUTHOR,str);
                        values.put(BookContract.BookEntry.COLUMN_LINK,link);
                        values.put(BookContract.BookEntry.COLUMN_IMAGE_LINK,imageLink);
                        values.put(BookContract.BookEntry.COLUMN_PUBLISHER_NAME,publishers);
                        values.put(BookContract.BookEntry.COLUMN_RATING,ratings);
                        Uri newUri=mContext.getContentResolver().insert(BookContract.BookEntry.CONTENT_URI,values);
                        if(newUri!=null){
                            Toast.makeText(mContext,"Book Saved Successfully",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext,"Book Not Saved",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        Intent shareintent=new Intent(Intent.ACTION_SEND);
                        shareintent.setType("text/plain");
                        StringBuilder builder1=new StringBuilder();
                        if(authors==null){
                            str="No Author Provided";
                        }else {
                            for (String names : authors) {
                                builder1.append(names + " , ");
                            }

                            str=builder1.toString();
                            str=str.substring(0,str.length()-2);
                        }
                        shareintent.putExtra(Intent.EXTRA_TEXT,mContext.getString(R.string.amazing_book)+title + " By " + authors +"\n\n" +"Here's the Preview Link :\n"+link +"\n\n" +mContext.getString(R.string.do_read));
                        mContext.startActivity(Intent.createChooser(shareintent,"share Book"));

                }
                }});
        AlertDialog dialog=builder.create();
        dialog.show();
}

private boolean checkIfBookAlreadyExist(String title){
    String selection="name=?";
    String[] SelectionArgs=new String[]{title};
    Cursor c=mContext.getContentResolver().query(BookContract.BookEntry.CONTENT_URI,null,selection,SelectionArgs,null);
    boolean exist= (c.getCount()>0);
    c.close();
    return exist;
}

}
