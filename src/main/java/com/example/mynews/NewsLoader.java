package com.example.mynews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.net.URL;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String myUrl;
    public NewsLoader(Context context, String  url)
    {
        super(context);
        myUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (myUrl == null)
        {
            return null;
        }
        /**
         * making request and getting response from API in background thread
         * and storing the parsed data in List
         *
         */
        List<News> myList = JsonHelper.extractJsonDetails(myUrl);
        return myList;
    }
}
