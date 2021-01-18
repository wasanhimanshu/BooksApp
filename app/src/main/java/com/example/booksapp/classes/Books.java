package com.example.booksapp.classes;

import java.util.List;

public class Books {
    private String mTitle;
    private List<String> mAuthor;
    private String mDescription;
    private String mLink;
    private String mImageLink;
    private String mPublisher;
    private String mPublishedDate;
    private String mRating;
    public Books(String title,List<String>author,String description,String link,String ImageLink,String publisher,String publisheddate,String rating){
        mTitle=title;
        mAuthor=author;
        mDescription=description;
        mLink=link;
        mImageLink=ImageLink;
        mPublisher=publisher;
        mPublishedDate=publisheddate;
        mRating=rating;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmTitle() {
        return mTitle;
    }

    public List<String> getmAuthor() {
        return mAuthor;
    }

    public String getmLink() {
        return mLink;
    }


    public String getmImageLink() {
        return mImageLink;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }

    public String getmRating() {
        return mRating;
    }
}
