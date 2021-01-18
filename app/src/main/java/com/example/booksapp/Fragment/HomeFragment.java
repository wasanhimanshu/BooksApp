package com.example.booksapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.booksapp.Adapter.TopicsAdapter;
import com.example.booksapp.R;
import com.example.booksapp.ResultActivity;
import com.example.booksapp.classes.Topics;

import java.util.ArrayList;

public class HomeFragment extends Fragment{
    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View rootView= inflater.inflate(R.layout.home_fragment,container,false);
        ArrayList<Topics>topics=new ArrayList<>();
        topics.add(new Topics("Romance",R.drawable.romance));
        topics.add(new Topics("Science",R.drawable.sciencefiction));
        topics.add(new Topics("Adventure",R.drawable.adventure));
        topics.add(new Topics("Horror",R.drawable.horror));
        topics.add(new Topics("Mystery",R.drawable.mystery));
        topics.add(new Topics("Suspense",R.drawable.suspense));
        topics.add(new Topics("History",R.drawable.history));
        topics.add(new Topics("Drama",R.drawable.drama));
        topics.add(new Topics("Politics",R.drawable.politcs1));
        topics.add(new Topics("Religion",R.drawable.religion));
        topics.add(new Topics("Education",R.drawable.education2));
        topics.add(new Topics("Travel",R.drawable.travel));
        topics.add(new Topics("War",R.drawable.war));

        TopicsAdapter mAdapter=new TopicsAdapter(getActivity(),topics);
        GridView gridView=(GridView)rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConnectivityManager cr =
                        (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeinfo=cr.getActiveNetworkInfo();
                if(activeinfo!=null&&activeinfo.isConnected()){
                    Intent i=new Intent(getActivity(), ResultActivity.class);
                    i.putExtra("topicname",topics.get(position).getmTitle());
                    startActivity(i);

                }
                else{
                    Toast.makeText(getActivity(),getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
                }
            }
        });
     return rootView;
    }
}
