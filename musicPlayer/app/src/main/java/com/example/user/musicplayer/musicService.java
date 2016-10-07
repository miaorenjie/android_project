package com.example.user.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import android.os.Handler;
import android.view.View;
import android.widget.RemoteViews;

import static com.example.user.musicplayer.Content.*;
import static com.example.user.musicplayer.Content.mode_flag;


/**
 * Created by user on 2016/9/22.
 */
public class musicService extends Service {
    private MediaPlayer player;
    private int nowposition;
    private int playposition;
    private int mode_flag = 0;
    private int clickposition;
    private boolean ischoose;
    private boolean isplay = false;
    private Random r = new Random();
    private boolean isfirstcome = true;
    //    private Handler handler;
    private boolean juge = false;


    private final int PLAYING = 1;
    private final int PAUSE = 2;
    private final int MODE = 3;
    private final int PREV = 4;
    private final int NEXT = 5;
    private final int SKIP = 6;
    private final int BACK = 7;
    private final int CLICKITEM = 8;
    private final int SETPLAY = 9;
    private final int SETPAUSE = 10;
    private final int UPDATE = 11;
    private final int PLAY = 12;
    private final int PROGRESSCHANGED = 13;
    private final int UPDATEPROGRESS = 14;
    private final int CREAT=555;
    private NotificationManager manager;

    private musicReceiver rv = new musicReceiver();


    @Override
    public void onDestroy() {
        player.release();
        manager.cancel(0);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("GET");
        musicReceiver musicreceiver = new musicReceiver();
        registerReceiver(musicreceiver, intentFilter);
        player = new MediaPlayer();


        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            Intent intent1 = new Intent("UPDATE");
            Intent intent2 = new Intent("PLAYINGUPDATE");

            @Override
            public void onCompletion(MediaPlayer mp) {

                switch (mode_flag) {
                    case 0:
                    case 1:
                        if (nowposition == mydata.size() - 1)
                            nowposition = 0;
                        else
                            nowposition++;
                        break;
                    case 2:
                        nowposition = r.nextInt(mydata.size());
                        break;
                    case 3:
                        break;
                }
                mp.reset();
                try {
                    mp.setDataSource(mydata.get(nowposition).getUri());
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                update_Notification();
                intent1.putExtra("control", UPDATE);
                intent1.putExtra("position", nowposition);
                intent1.putExtra("playposition", playposition);
                intent1.putExtra("isplay", isplay);
                intent1.putExtra("ischoose", ischoose);
                sendBroadcast(intent1);


                intent2.putExtra("control", UPDATE);
                intent2.putExtra("position", nowposition);
                intent2.putExtra("playposition", playposition);
                intent2.putExtra("isplay", isplay);
                intent2.putExtra("ischoose", ischoose);
                sendBroadcast(intent2);
//                }
//                juge = false;
            }
        });

        new Thread() {
            Intent intent = new Intent("PLAYINGUPDATE");

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (player.isPlaying() && ischoose)
                        intent.putExtra("nowDruation", player.getCurrentPosition());
                    intent.putExtra("control", UPDATEPROGRESS);
                    intent.putExtra("position", nowposition);
                    intent.putExtra("playposition", playposition);
                    intent.putExtra("isplay", player.isPlaying());
                    intent.putExtra("ischoose", ischoose);
                    intent.putExtra("mode_flag", mode_flag);
                    sendBroadcast(intent);
//                    Log.i("发送成功！",""+player.getCurrentPosition());
                }
            }
        }.start();
        super.onCreate();
    }

    private void update_Notification() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);


        Bitmap bmp=mydata.get(nowposition).getListcover();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) 300.0) / bmp.getWidth(), ((float) 300.0) / bmp.getHeight());
        Bitmap real = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        remoteViews.setImageViewBitmap(R.id.notiImage, real);

        remoteViews.setTextViewText(R.id.notiName, mydata.get(nowposition).getPlayName());
        remoteViews.setTextViewText(R.id.notiartist, mydata.get(nowposition).getArtistName());

        remoteViews.setImageViewResource(R.id.noti_bt_per, R.mipmap.noti_prev);
        if(!player.isPlaying())
             remoteViews.setImageViewResource(R.id.noti_bt_play,R.mipmap.noti_play);
        else
            remoteViews.setImageViewResource(R.id.noti_bt_play,R.mipmap.noti_pause);
        remoteViews.setImageViewResource(R.id.noti_bt_next, R.mipmap.noti_next);

        PendingIntent skipIntent=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);

        Intent prebuttonIntent=new Intent("GET");
        prebuttonIntent.putExtra("control", PREV);
        PendingIntent preintent=PendingIntent.getBroadcast(this,0,prebuttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_bt_per, preintent);

        Intent playbuttonIntent=new Intent("GET");
        playbuttonIntent.putExtra("control", PLAY);
        PendingIntent playintent=PendingIntent.getBroadcast(this,1,playbuttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_bt_play, playintent);

        Intent nextbuttonIntent=new Intent("GET");
        nextbuttonIntent.putExtra("control", NEXT);
        PendingIntent nextintent=PendingIntent.getBroadcast(this,2,nextbuttonIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_bt_next, nextintent);

        Notification notification=new NotificationCompat.Builder(this).
                setContent(remoteViews).
                setContentTitle("正在播放的音乐是:"+mydata.get(nowposition).
                        getPlayName()).
                setSmallIcon(R.mipmap.icon).
                setContentText(mydata.get(nowposition).getPlayName()).
                setOngoing(true).
                setContentIntent(skipIntent)
                .build();

        manager.notify(0,notification);
    }

    private class musicReceiver extends BroadcastReceiver {
        int control;
        int controlplaying;
        Intent intent1 = new Intent("UPDATE");
        Intent intent2 = new Intent("PLAYINGUPDATE");

        @Override
        public void onReceive(Context context, Intent intent) {
            control = intent.getIntExtra("control", -1);
            controlplaying = intent.getIntExtra("controlplaying", -1);
            switch (control) {
                case PLAY:
                    if (isplay) {
                        isplay = false;
                        playposition = player.getCurrentPosition();
                        player.pause();
                    } else {
                        isplay = true;
                        player.reset();
                        try {
                            player.setDataSource(mydata.get(nowposition).getUri());

                            player.prepare();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        player.seekTo(playposition);
                    }
                    if (controlplaying != -1)
                        isfirstcome = false;
                    ischoose = true;
                    Log.e("asd","PLAY");
                    update_Notification();
                    break;
                case PLAYING:
                    break;
                case PAUSE:
                    break;
                case MODE:
                    if (mode_flag == 3)
                        mode_flag = 0;
                    else
                        mode_flag++;
                    break;
                case PREV:
                    switch (mode_flag) {
                        case 0:
                        case 1:
                            if (nowposition == 0)
                                nowposition = mydata.size() - 1;
                            else
                                nowposition--;
                            break;
                        case 2:
                            nowposition = r.nextInt(mydata.size());
                            break;
                        case 3:
                            break;
                    }
                    try {
                        player.reset();
                        player.setDataSource(mydata.get(nowposition).getUri());
                        player.prepare();
                        player.start();
                        isplay = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ischoose = true;
                    if (controlplaying != -1)
                        isfirstcome = false;
                    juge = true;
                    update_Notification();
                    Log.e("asd", "PREV");
                    break;
                case NEXT:
                    switch (mode_flag) {
                        case 0:
                        case 1:
                            if (nowposition == mydata.size() - 1)
                                nowposition = 0;
                            else
                                nowposition++;
                            break;
                        case 2:
                            nowposition = r.nextInt(mydata.size());
                            break;
                        case 3:
                            break;
                    }

                    try {
                        player.reset();
                        player.setDataSource(mydata.get(nowposition).getUri());
                        player.prepare();
                        player.start();
                        isplay = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ischoose = true;
                    if (controlplaying != -1)
                        isfirstcome = false;
                    juge = true;
                    update_Notification();
                    Log.e("asd", "NEXT");
                    break;
                case SKIP:
                    if (ischoose)
                        playposition = player.getCurrentPosition();


                    break;
                case BACK:


                    break;
                case CLICKITEM:
                    clickposition = intent.getIntExtra("clickposition", 0);
                    if (isplay && clickposition == nowposition) {
                        playposition = player.getCurrentPosition();
                        player.pause();
                        isplay = false;

                        nowposition = clickposition;
                    } else {
                        try {

                            player.reset();
                            player.setDataSource(mydata.get(clickposition).getUri());
                            player.prepare();
                            player.start();
                            if (nowposition == clickposition)
                                player.seekTo(playposition);
                            isplay = true;
                            nowposition = clickposition;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ischoose = true;
                    juge = true;
                    update_Notification();
                    break;
                case PROGRESSCHANGED:

                    if (!isplay) {
                        isplay = true;
                        player.reset();

                        try {
                            player.setDataSource(mydata.get(nowposition).getUri());

                            player.prepare();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        player.seekTo((int) (float) intent.getIntExtra("progress", 0) / intent.getIntExtra("seekMax", 0) * player.getDuration());

                    } else
                        player.seekTo((int) ((float) intent.getIntExtra("progress", 0) / intent.getIntExtra("seekMax", 0) * player.getDuration()));

                    break;
                case CREAT:
                    break;
            }

            intent1.putExtra("control", UPDATE);
            intent1.putExtra("position", nowposition);
            intent1.putExtra("playposition", playposition);
            intent1.putExtra("isplay", isplay);
            intent1.putExtra("ischoose", ischoose);
            intent1.putExtra("mode_flag", mode_flag);
            sendBroadcast(intent1);


            intent2.putExtra("control", UPDATE);
            intent2.putExtra("position", nowposition);
            intent2.putExtra("playposition", playposition);
            intent2.putExtra("isplay", isplay);
            intent2.putExtra("ischoose", ischoose);
            intent2.putExtra("mode_flag", mode_flag);
            sendBroadcast(intent2);
        }
    }
}

