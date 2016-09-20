package com.example.user.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by user on 2016/9/11.
 */
public class music {
    private String playName;
    private String artistName;
    private Bitmap listcover;
    private Bitmap playcover;
    private String uri;


    private long duration1;


    public long getMusicduration() {
        return musicduration;
    }

    public void setMusicduration(long musicduration) {
        this.musicduration = musicduration;
    }

    private long musicduration;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Bitmap getPlaycover() {
        return playcover;
    }

    public void setPlaycover(Bitmap playcover) {
        this.playcover = playcover;
    }

    public Bitmap getListcover() {

        return listcover;
    }

    public void setListcover(Bitmap listcover) {
        this.listcover = listcover;
    }

    private long duration;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getArtistName() {

        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPlayName() {

        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

}
