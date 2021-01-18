package com.example.booksapp.classes;

public class Topics {
    private String mTitle;
    private static final int IMAGE_NOT_PROVIDED=-1;
    private int mImageResourceId=IMAGE_NOT_PROVIDED;
    public Topics(String title,int  imageResouceId){
        mTitle=title;
        mImageResourceId=imageResouceId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }
    public boolean hasImage(){
        return mImageResourceId!=IMAGE_NOT_PROVIDED;
    }
}
