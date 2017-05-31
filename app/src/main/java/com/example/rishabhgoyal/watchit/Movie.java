package com.example.rishabhgoyal.watchit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rishabh Goyal on 31-05-2017.
 */

public class Movie implements Parcelable {

    private String mTitle;
    private String mRelaseDate;
    private String mPlot;
    private Double mVoteAverage;
    private String mImagePoster;

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mTitle);
        dest.writeString(mRelaseDate);
        dest.writeString(mPlot);
        dest.writeString(mImagePoster);
        dest.writeDouble(mVoteAverage);
    }
    protected Movie(Parcel in) {

        this.mTitle=in.readString();
        this.mImagePoster=in.readString();
        this.mPlot=in.readString();
        this.mRelaseDate=in.readString();
        this.mVoteAverage=in.readDouble();
    }
    public Movie(){

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public String getTitle(){
        return mTitle;
    }
    public void setTitle(String title)
    {
        mTitle=title;
    }

    public String getRelaseDate(){
        return mRelaseDate;
    }
    public void setRelaseDate(String date)
    {
        mRelaseDate=date;
    }
    public String getImage(){
        return mImagePoster;
    }
    public void setImage(String image)
    {
        mImagePoster=image;
    }
    public String getPlot(){
        return mPlot;
    }
    public void setPlot(String plot)
    {
        mPlot=plot;
    }
    public Double getVote(){
        return mVoteAverage;
    }
    public void setVote(Double vote)
    {
        mVoteAverage=vote;
    }

}
