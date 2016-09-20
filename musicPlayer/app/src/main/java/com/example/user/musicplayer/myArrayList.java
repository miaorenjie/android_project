package com.example.user.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 2016/9/14.
 */
public class myArrayList<E> extends ArrayList<E>  implements Serializable {
    private music mydata;

    public Object getMydata() {
        return mydata;
    }

    public void setMydata(music mydata) {
        this.mydata = mydata;
    }
}
