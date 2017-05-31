package com.example.rishabhgoyal.watchit;


import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Rishabh Goyal on 31-05-2017.
 */

public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<Movie> extractResultFromJson(String movieJSON) {
        String IMAGE_BASE_URL =  "http://image.tmdb.org/t/p/";
        String IMAGE_SIZE =  "w185";
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject reader=new JSONObject(movieJSON);
            JSONArray results=reader.getJSONArray("results");
            for(int i=0;i<results.length();i++)
            {
                JSONObject movie=results.getJSONObject(i);
                String title=movie.getString("title");
                String date=movie.getString("release date");
                Double vote=movie.getDouble("vote_average");
                String plot=movie.getString("plot");
                String posterPath = IMAGE_BASE_URL + IMAGE_SIZE + movie.getString("poster_path");
                Movie m=new Movie();
                m.setTitle(title);
                m.setPlot(plot);
                m.setImage(posterPath);
                m.setVote(vote);
                m.setRelaseDate(date);
                movies.add(m);

            }
            } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }


    public static List<Movie> fetchMovieData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Movie> movie = extractResultFromJson(jsonResponse);

        // Return the {@link Event}
        return movie;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
