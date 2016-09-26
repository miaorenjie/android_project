package com.example.user.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.provider.MediaStore.Audio.Media;

/**
 * Created by user on 2016/9/11.
 */
public class musicUtil {
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public static String formatTimeInt(int time) {
        String min = time / 60 + "";
        String sec = time % 60 + "";
        if (min.length() < 2) {
            min = "0" + time / 60 + "";
        } else {
            min = time / 60 + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % 60) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % 60) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % 60) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % 60) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public static ArrayList<music> read(Context context) {
        ArrayList<music> mydata = new ArrayList<music>();
        long albumid;
        int index;
        long songid;
        String name1;
        String[] projection = {
                Media.DISPLAY_NAME,
                Media._ID,
                Media.DATA,
                Media.ALBUM_ID,
                Media.ARTIST,
                Media.DURATION,
        };

        ContentResolver cr = context.getContentResolver();
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.zerolog);
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null, null);
        while (cursor.moveToNext()) {
            music data = new music();
            name1=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            index =name1.lastIndexOf(".");
            name1=name1.substring(0,index);
            data.setPlayName(name1);
             songid= cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            data.setUri(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            data.setArtistName(cursor.getString(cursor.getColumnIndex(Media.ARTIST)));
            data.setDuration(cursor.getInt(cursor.getColumnIndex(Media.DURATION)));
            Log.i("!!!!!!!!!!!","11");
//            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songid + "/albumart");
                Log.i("id:", "" + songid);
                ParcelFileDescriptor pfd = null;

                try {
                    pfd = cr.openFileDescriptor(uri, "r");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                FileDescriptor fd = null;
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                    Log.i("加载成功", "11111");


                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd);
                    Matrix matrix = new Matrix();
                    matrix.postScale(((float) 250.0) / bitmap.getWidth(), ((float) 250.0) / bitmap.getHeight());
                    Bitmap real = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    data.setListcover(real);

                    Matrix matrix1 = new Matrix();
                    matrix1.postScale(((float) 750) / bitmap.getWidth(), ((float) 750) / bitmap.getHeight());
                    Bitmap real1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);
                    data.setListcover(real);

                    data.setPlaycover(real1);
                } else {

                    data.setListcover(bmp);
                    data.setPlaycover(bmp);
                    Log.i("没有加载成功！", "2222");
                }

                mydata.add(data);
//            } else {
//                Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumid);
//                ParcelFileDescriptor pfd = null;
//                try {
//                    pfd = cr.openFileDescriptor(uri, "r");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                FileDescriptor fd = null;
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                if (pfd != null) {
//                    fd = pfd.getFileDescriptor();
//                    Log.i("else加载成功", "11111");
//
//
//                    Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd);
//
//                    Matrix matrix = new Matrix();
//                    matrix.postScale(((float) 250.0) / bitmap.getWidth(), ((float) 250.0) / bitmap.getHeight());
//                    Bitmap real = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                    data.setListcover(real);
//
//                    data.setPlaycover(bitmap);
//
//                } else {
//
//                    data.setListcover(bmp);
//                    data.setPlaycover(bmp);
//                    Log.i("else没有加载成功！", "2222");
//                }
//
//
//                mydata.add(data);
//            }

        }
        cursor.close();
        return mydata;
    }

}
