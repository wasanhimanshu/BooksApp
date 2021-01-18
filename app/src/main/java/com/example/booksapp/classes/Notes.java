package com.example.booksapp.classes;

public class Notes {
    private String mTitle;
    private  String mSummary;

    public Notes(String title,String Summary){
        mTitle=title;
        mSummary=Summary;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSummary() {
        return mSummary;
    }

}
