package com.example.rishabhgoyal.watchit;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Rishabh Goyal on 31-05-2017.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl.length() < 1 || mUrl == null) {
            return null;
        }
        List<Movie> movie = QueryUtils.fetchMovieData(mUrl);
        return movie;
    }
}