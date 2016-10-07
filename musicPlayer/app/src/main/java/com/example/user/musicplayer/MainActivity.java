package com.example.user.musicplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.provider.MediaStore.Audio.Media;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.os.Handler;

import static com.example.user.musicplayer.Content.*;
import static com.example.user.musicplayer.PlayingActivity.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView playname;
    private TextView artistname;
    private ImageView tvtile;
    private ListView lvplaylist;
    private ImageView btper;
    private ImageView btplay;
    private ImageView btnext;

    private int myposition;
    private musicAdapter adapter;

    private Boolean isplay = false;
    private int playposition = 0;
    private int nowposition = 0;
    private RelativeLayout relativeLayout;
    private String TAG = "111111---------------------------";
    private Random r;
    private ProgressBar progressBar;
    private Handler handler;
    private boolean ischoose = false;
    private boolean mainpause = true;

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
    private final int CREAT=555;

    private class MainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("control", 0);
            switch (control) {
                case UPDATE:
                    Log.i("成功", "MainReceiver");
                    isplay = intent.getBooleanExtra("isplay", false);
                    nowposition = intent.getIntExtra("position", 0);
                    playposition = intent.getIntExtra("playposition", 0);
                    ischoose = intent.getBooleanExtra("ischoose", false);
                    break;

            }
            setinfo();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE");
        MainReceiver musicreceiver = new MainReceiver();
        registerReceiver(musicreceiver, intentFilter);


        setContentView(R.layout.main);
        init();

        Log.e(TAG, "start onCreate~~~");
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        handler=new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what)
//                {
//                    case 0:
//                        progressBar.setProgress((int)((float)player.getCurrentPosition()/player.getDuration()));
//                        break;
//                }
//            }
//        };
//
//        ActivityManager myManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
//        for (int i = 0; i < runningService.size(); i++) {
//            if (runningService.get(i).service.getClassName().toString().equals("com.example.user.musicplayer.musicService")) {
//
//                Intent intent = new Intent(this, musicService.class);
//                startService(intent);
//            }
//        }

        Intent intent = new Intent(this, musicService.class);
        startService(intent);
        Intent intent1=new Intent("GET");
        intent1.putExtra("control",CREAT);
        sendBroadcast(intent1);
    }

        @Override
        protected void onStart () {
            super.onStart();
            Log.e(TAG, "start onStart~~~");
            if (isplay)
                btplay.setImageResource(R.mipmap.pause);
            else
                btplay.setImageResource(R.mipmap.play);

//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Log.e(TAG, "start onCompletion~~~");
//                switch (mode_flag) {
//                    case 0:
//                    case 1:
//                    case 3:
//
//                        if (nowposition == mydata.size() - 1)
//                            nowposition = 0;
//                        else
//                            nowposition++;
//                        break;
//                    case 2:
//                        nowposition = r.nextInt(mydata.size());
//                        break;
//                }
//                mp.reset();
//                try {
//                    mp.setDataSource(mydata.get(nowposition).getUri());
//                    mp.prepare();
//                    mp.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                setinfo();
//            }
//        });


//
//        new Thread(){
//            @Override
//            public void run() {
//                while(true)
//                {
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Message message=new Message();
//                    message.what=0;
//                    handler.sendMessage(message);
//
//                    if (isactivitypause)
//                        break;
//                }
//            }
//        }.start();


        }
        @Override
        protected void onRestart () {
            super.onRestart();
            Log.e(TAG, "start onRestart~~~");

        }
        @Override
        protected void onResume () {
            super.onResume();
            Log.e(TAG, "start onResume~~~");
        }
        @Override
        protected void onPause () {
            super.onPause();
            Log.e(TAG, "start onPause~~~");

        }
        @Override
        protected void onStop () {
            super.onStop();
//        player.stop();
//        player.release();
            Log.e(TAG, "start onStop~~~");
        }
        @Override
        protected void onDestroy () {
            super.onDestroy();
            Log.e(TAG, "start onDestroy~~~");
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == 2) {
                Log.e(TAG, "start onActivityResult~~~");
                playposition = data.getIntExtra("playposition", 0);
                isplay = data.getBooleanExtra("isplay", false);
                nowposition = data.getIntExtra("position", 0);
                tvtile.setImageBitmap(mydata.get(nowposition).getListcover());
                if (isplay) {
                    btplay.setImageResource(R.mipmap.pause);
                } else {
                    btplay.setImageResource(R.mipmap.play);
                }
                setinfo();
            }


        }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mydata = musicUtil.read(this);
                    adapter = new musicAdapter(this, mydata);
                    lvplaylist.setAdapter(adapter);
                    lvplaylist.setOnItemClickListener(this);


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }
        }
    }

    public void init() {
        r = new Random();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.playcover);
        Matrix matrix = new Matrix();
        matrix.postScale(((float) 250.0) / bitmap.getWidth(), ((float) 250.0) / bitmap.getHeight());
        Bitmap real = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//
//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.play);
//        Matrix matrix1 = new Matrix();
//        matrix1.postScale(((float) 125) / bitmap1.getWidth(), ((float) 125) / bitmap1.getHeight());
//        Bitmap real1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix1, true);
//
//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.next);
//        Matrix matrix2 = new Matrix();
//        matrix2.postScale(((float) 100) / bitmap2.getWidth(), ((float) 100) / bitmap2.getHeight());
//        Bitmap real2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);
//
//        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.per);
//        Matrix matrix3 = new Matrix();
//        matrix3.postScale(((float) 100) / bitmap3.getWidth(), ((float) 100) / bitmap3.getHeight());
//        Bitmap real3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix3, true);
        //  adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        relativeLayout = (RelativeLayout) findViewById(R.id.playbar);
        relativeLayout.setOnClickListener(this);
//        player = new MediaPlayer();
        btplay = (ImageView) findViewById(R.id.play);
        //    btplay.setImageBitmap(real1);
        btplay.setOnClickListener(this);
        lvplaylist = (ListView) findViewById(R.id.musiclist1);
        btnext = (ImageView) findViewById(R.id.next);
//        btnext.setImageBitmap(real2);
        btnext.setOnClickListener(this);
        btper = (ImageView) findViewById(R.id.per);
        btper.setOnClickListener(this);
//        btper.setImageBitmap(real3);
        tvtile = (ImageView) findViewById(R.id.playcover);
        tvtile.setImageBitmap(real);
        playname = (TextView) findViewById(R.id.textplayname);
        artistname = (TextView) findViewById(R.id.textartistname);
//        progressBar= (ProgressBar) findViewById(R.id.progressbar);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent("GET");
        switch (v.getId()) {
            case R.id.playbar:
//                if(ischoose)
//                     playposition=player.getCurrentPosition();
                Log.e(TAG, "start playposition~~~" + playposition);
//                Log.i("main", "" + playposition);
//                player.stop();
                //    player.release();
//                Toast.makeText(MainActivity.this, "111", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, PlayingActivity.class);
//                Intent intent1 = new Intent(this, PlayingActivity.class);
//                intent1.putExtra("position",nowposition);
//                intent1.putExtra("playposition", playposition);
//                intent1.putExtra("isplay", isplay);
//                intent1.putExtra("ischoose",ischoose);
//                startActivityForResult(intent1, 1);
                startActivity(intent1);
//                setContentView(R.layout.activity_playing);

                intent.putExtra("control", SKIP);
                sendBroadcast(intent);
                break;

            case R.id.per:
//                switch (mode_flag) {
//                    case 0:
//                    case 1:
//                    case 3:
//                        if (nowposition == 0)
//                            nowposition = mydata.size() - 1;
//                        else
//                            nowposition--;
//                        break;
//                    case 2:
//                        nowposition = r.nextInt(mydata.size());
//                        break;
//                }
//
//                try {
//                    player.reset();
//                    player.setDataSource(mydata.get(nowposition).getUri());
//                    player.prepare();
//                    player.start();
//                    setinfo();
//                    isplay=true;
//                    btplay.setImageResource(R.mipmap.pause);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                intent.putExtra("control", PREV);
                sendBroadcast(intent);
                break;
            case R.id.play:
//                if(isplay)
//                {
//                    isplay=false;
//                    mainpause=true;
//                    playposition=player.getCurrentPosition();
//                    player.pause();
//                    mainpause=true;
//                    btplay.setImageResource(R.mipmap.play);
//                }
//                else
//                {
//                    isplay=true;
//                    player.reset();
//
//                    try {
//                        player.setDataSource(mydata.get(nowposition).getUri());
//
//                        player.prepare();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    player.start();
//                    player.seekTo(playposition);
//                    btplay.setImageResource(R.mipmap.pause);
//                    mainpause=false;
//                }
//                setinfo();
                intent.putExtra("control", PLAY);
                sendBroadcast(intent);
                break;
            case R.id.next:
//                switch (mode_flag) {
//                    case 0:
//                    case 1:
//                    case 3:
//                        if (nowposition == mydata.size() - 1)
//                            nowposition = 0;
//                        else
//                            nowposition++;
//                        break;
//                    case 2:
//                        nowposition = r.nextInt(mydata.size());
//                        break;
//                }
//
//                try {
//                    player.reset();
//                    player.setDataSource(mydata.get(nowposition).getUri());
//                    player.prepare();
//                    player.start();
//                    setinfo();
//                    isplay=true;
//                    btplay.setImageResource(R.mipmap.pause);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mainpause=false;
//                break;
                intent.putExtra("control", NEXT);
                sendBroadcast(intent);
                break;
        }
    }

    private void setinfo() {
        playname.setText(mydata.get(nowposition).getPlayName());
        artistname.setText(mydata.get(nowposition).getArtistName());
        tvtile.setImageBitmap(mydata.get(nowposition).getListcover());
        Log.e("!!!!!", "" + isplay);
        if (isplay)
            btplay.setImageResource(R.mipmap.pause);
        else
            btplay.setImageResource(R.mipmap.play);
        ischoose = true;
//        progressBar.setMax(player.getDuration());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (isplay && position == nowposition) {
//            player.pause();
//            isplay = false;
//            mainpause=true;
//            btplay.setImageResource(R.mipmap.play);
//            playposition = player.getCurrentPosition();
//            Log.i("info", "" + position);
//            setinfo();
//            nowposition = position;
//        } else {
//            try {
//
//                player.reset();
//                player.setDataSource(mydata.get(position).getUri());
//                player.prepare();
//                player.start();
//                if (nowposition == position)
//                    player.seekTo(playposition);
//                isplay = true;
//                mainpause=false;
//                nowposition = position;
//                Log.i("info", "" + position);
//                btplay.setImageResource(R.mipmap.pause);
//                setinfo();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        ischoose=true;
        Intent intent = new Intent("GET");
        intent.putExtra("control", CLICKITEM);
        intent.putExtra("clickposition", position);
        sendBroadcast(intent);
    }
}
