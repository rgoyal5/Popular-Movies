package com.example.rishabhgoyal.watchit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TextView mMovieTitleTextView;
    @BindView(R.id.movie_date)
    TextView mMovieDateTextView;
    @BindView(R.id.image)
    ImageView mMovieImageView;
    @BindView(R.id.movie_vote_average)
    TextView mMovieVoteAverageTextView;
    @BindView(R.id.movie_overview)
    TextView mMoviePlotSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Movie movie = (Movie) getIntent().getParcelableExtra("myData");

        mMovieTitleTextView.setText(movie.getTitle());
        mMovieDateTextView.setText(movie.getRelaseDate().substring(0,4));
        Picasso.with(mMovieImageView.getContext())
                .load(movie.getImage())
                .into(mMovieImageView);
        mMovieVoteAverageTextView.setText(movie.getVote()+ "/10");
        mMoviePlotSynopsisTextView.setText(movie.getPlot());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
