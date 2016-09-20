package com.example.user.musicplayer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 2016/9/16.
 */
public class passValue implements Serializable{
    private ArrayList<music> data;

    public passValue(ArrayList<music> data)
    {
        this.data=data;
    }
    public ArrayList<music> getData() {
        return data;
    }

    public void setData(ArrayList<music> data) {
        this.data = data;
    }
}
