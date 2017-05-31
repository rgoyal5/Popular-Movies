package com.example.rishabhgoyal.watchit;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>  {
    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private int mPage = 1;
    private String mSortBy;

    private static RecyclerView mRecyclerView;
    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingIndicator;

    private static final int MOVIE_LOADER_ID = 1;

    private static final String MOVIE_REQUEST_URL =
            "https://api.themoviedb.org/3/movie/550?api_key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_view);

        mEmptyStateTextView=(TextView)findViewById(R.id.empty_view);
        mLayoutManager = new GridLayoutManager(this, 3);
        mLoadingIndicator=(ProgressBar)findViewById(R.id.loading_indicator);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);



        mMovieAdapter = new MovieAdapter(new ArrayList<Movie>(), new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("myData", movie);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mMovieAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerScrollListenere(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPage = page;
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSortBy = sharedPrefs.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default)
        );

        Uri baseUri = Uri.parse(MOVIE_REQUEST_URL + mSortBy);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api_key", "65789011bea02377247858b34fa51974");
        uriBuilder.appendQueryParameter("language","en-US");
        uriBuilder.appendQueryParameter("page", String.valueOf(mPage));

        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movies != null && !movies.isEmpty()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.GONE);
            mMovieAdapter.addAll(movies);
            Log.d("itemCount", String.valueOf(mMovieAdapter.getItemCount()));
        }else {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_movie);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mMovieAdapter.clear();
    }
}
