package com.example.booksapp;

import android.app.DownloadManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.booksapp.classes.Books;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG=QueryUtils.class.getSimpleName();
    private QueryUtils(){}

    public static List<Books> fetchBooks(String mUrl){
        URL url=createUrl(mUrl);
        String jsonResponse=null;
        try{
jsonResponse=MakeHttpConnection(url);
        }catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
List<Books> books=extractBooksDta(jsonResponse);
        return books;

    }

    private static URL createUrl(String mUrl){
        URL url=null;
        try{
            url=new URL(mUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static final String MakeHttpConnection(URL url) throws IOException{
        String jsonResponse="";
        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream =null;
        try{
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromInputStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem retrieving the News JSON results.", e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromInputStream(InputStream inputStream) throws IOException{
       StringBuilder builder=new StringBuilder();
       if(inputStream!=null){
           InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
           BufferedReader reader=new BufferedReader(inputStreamReader);
           String line=reader.readLine();
           while(line!=null){
               builder.append(line);
               line=reader.readLine();
           }
       }
return builder.toString();
    }
    private static final List<Books> extractBooksDta(String jsonresponse){
        if(TextUtils.isEmpty(jsonresponse)){
            return null;
        }
        List<Books> newBooks=new ArrayList<>();
        try{
            JSONObject baseJSonresponse=new JSONObject(jsonresponse);
            JSONArray jsonArray=baseJSonresponse.getJSONArray("items");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject currbook=jsonArray.getJSONObject(i);
                JSONObject info=currbook.getJSONObject("volumeInfo");
                String name=info.getString("title");

                 String Link=info.getString("previewLink");
           List< String>authornames ;
                if(!info.has("authors")){
                    authornames=null;
                }else{
                    JSONArray arrayAuthornames =info.getJSONArray("authors");

                  authornames=new ArrayList<>();
                    for (int j = 0; j < arrayAuthornames.length(); j++) {
                        String authorname = arrayAuthornames.getString(j);
                        authornames.add(authorname);
                    }
                }
                String publisher;
              if(!info.has("publisher")){
                  publisher=null;
              }else{
                  publisher=info.getString("publisher");
              }
                String Publisheddate;
              if(!info.has("punblishedDate")){
                  Publisheddate="No Date Provided";
              }else {
                  Publisheddate = info.getString("publishedDate");
              }
                String thumbnail, description;
                if (!info.has("imageLinks"))
                    thumbnail = null;
                else {// Extract the JSONObject for the key called "imageLinks"
                    JSONObject imagelinks =info.getJSONObject("imageLinks");

                    // Extract the value for the key called "thumbnail"
                    thumbnail = imagelinks.getString("thumbnail");
                }

                if (!info.has("description"))
                    description = null;
                else {
                    // Extract the value for the key called "description"
                    description =info.getString("description");
                }
                String rating;
                if(!info.has("averageRating")){
                    rating=null;
                }else{
                    rating=info.getString("averageRating");
                }
                Books books=new Books(name,authornames,description,Link,thumbnail,publisher,Publisheddate,rating);
                newBooks.add(books);

            }
        }catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the Books JSON results", e);
        }

        return newBooks;
    }

}
