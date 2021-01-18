package com.example.booksapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.booksapp.R;
import com.example.booksapp.classes.Topics;

import java.util.ArrayList;

public class TopicsAdapter extends ArrayAdapter<Topics> {
    public TopicsAdapter(Context context, ArrayList<Topics>topics){
        super(context,0,topics);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View gridItemView=convertView;
       if(gridItemView==null){
           gridItemView= LayoutInflater.from(getContext()).inflate(R.layout.grid_item,parent,false);
       }
        Topics topics=getItem(position);
        TextView title=(TextView)gridItemView.findViewById(R.id.topics_name);
        title.setText(topics.getmTitle());
        ImageView imageView=(ImageView)gridItemView.findViewById(R.id.topics_image);
        if(topics.hasImage()){

            imageView.setImageResource(topics.getmImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setVisibility(View.GONE);
        }
        return gridItemView;
    }
}
