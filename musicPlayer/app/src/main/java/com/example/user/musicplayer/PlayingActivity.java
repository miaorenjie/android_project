package com.example.user.musicplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.os.Handler;

import java.util.logging.LogRecord;
import java.util.zip.Inflater;

/**
 * Created by user on 2016/9/13.
 */
import static com.example.user.musicplayer.Content.*;

public class PlayingActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private TextView playingname;
    private ImageView playingcover;
    private ImageView mode;
    private ImageView per;
    private ImageView play;
    private ImageView next;
    private int playposition;
    private boolean isplay;
    private int position;
    private String TAG = "2222---------------------------";
    private RoundedBitmapDrawable rbd;
    private Random r;
    private SeekBar seekBar;

    private Handler handler;
    private boolean isdestory = false;
    private boolean isfirstcome = true;
    private boolean ischoose = false;
    private boolean mainpause;

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
    private final int UPDATEPROGRESS=14;

    public class PlayingReceiver extends BroadcastReceiver {
        int control;
        @Override
        public void onReceive(Context context, Intent intent) {
             control= intent.getIntExtra("control", 0);
//            Log.i("成功！","PlayingReceiver");
            switch (control) {
                case UPDATE:
//                    Log.i("成功！","PlayingReceiver");
                    isplay = intent.getBooleanExtra("isplay", false);
                    position = intent.getIntExtra("position", 0);
                    playposition = intent.getIntExtra("playposition", -1);
                    ischoose = intent.getBooleanExtra("ischoose", false);
                    mode_flag = intent.getIntExtra("mode_flag", 0);

                    switch (mode_flag) {
                        case 0:
                            mode.setImageResource(R.mipmap.mode_order);
                            break;
                        case 1:
                            mode.setImageResource(R.mipmap.mode_circulation);
                            break;
                        case 2:
                            mode.setImageResource(R.mipmap.mode_random);

                            break;
                        case 3:
                            mode.setImageResource(R.mipmap.mode_singlecycle);

                            break;
                    }
                    break;
                case UPDATEPROGRESS:
                    isplay = intent.getBooleanExtra("isplay", false);
                    position = intent.getIntExtra("position", 0);
                    playposition = intent.getIntExtra("nowDruation", -1);
                    ischoose = intent.getBooleanExtra("ischoose", false);
                    mode_flag = intent.getIntExtra("mode_flag", 0);


                    if (isplay && ischoose) {
                            seekBar.setProgress((int) (intent.getIntExtra("nowDruation", -1) * seekBar.getMax() /  mydata.get(position).getDuration()));
                        Log.i("改变成功！", "" + isplay + "-----------" + ischoose + "" + intent.getIntExtra("nowDruation", -1));
                    }
                    break;
            }
            setmusicInfo();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_playing);
//        Intent intent=new Intent(this,musicService.class);
//        startService(intent);

        IntentFilter intentFilter1=new IntentFilter();
        intentFilter1.addAction("PLAYINGUPDATE");
        PlayingReceiver playingReceiver=new PlayingReceiver();
        registerReceiver(playingReceiver, intentFilter1);
        init();
//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Log.e(TAG,"setOnCompletionListener");
//                switch (mode_flag) {
//                    case 0:
//                    case 1:
//                    case 3:
//                        if (position == mydata.size() - 1)
//                            position = 0;
//                        else
//                            position++;
//                        break;
//                    case 2:
//                        position = r.nextInt(mydata.size());
//                        break;
//                }
//                mp.reset();
//                try {
//                    mp.setDataSource(mydata.get(position).getUri());
//                    mp.prepare();
//                    mp.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                playingcover.setImageBitmap(mydata.get(position).getPlaycover());
//            }
//        });

//        new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(1);
//
//                        Message message = new Message();
//                        message.what = 0;
//                        handler.sendMessage(message);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    if(player.isPlaying())
//                    {
//                        Message message=new Message();
//                        message.what=1;
//                        handler.sendMessage(message);
//                    }
//                    else{
//                        Message message=new Message();
//                        message.what=2;
//                        handler.sendMessage(message);
//                    }
//                    if(isdestory)
//                        break;
//
//                }
//            }
//        }.start();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e(TAG, "start onStart~~~");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "start onRestart~~~");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "start onResume~~~");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "start onPause~~~");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "start onStop~~~");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "start onDestroy~~~");
        isdestory = true;
    }

    private void init() {

        r = new Random();


        playingname = (TextView) findViewById(R.id.playingname);
        playingcover = (ImageView) findViewById(R.id.playingcover);
        mode = (ImageView) findViewById(R.id.playingmode);
        mode.setImageResource(R.mipmap.mode_order);
        mode.setOnClickListener(this);
        per = (ImageView) findViewById(R.id.playingper);
        per.setOnClickListener(this);
        play = (ImageView) findViewById(R.id.playingplay);
        play.setOnClickListener(this);
        next = (ImageView) findViewById(R.id.playingnext);
        next.setOnClickListener(this);
        if (isplay)
            play.setImageResource(R.mipmap.playingpause);
        else
            play.setImageResource(R.mipmap.playingplay);
        seekBar = (SeekBar) findViewById(R.id.playingposition);
        seekBar.setOnSeekBarChangeListener(this);
        setmusicInfo();

//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 0:
//                        if (isplay && ischoose) {
//                            long duration = mydata.get(position).getDuration();
//                            long max = seekBar.getMax();
//                            seekBar.setProgress((int) (playposition * max / duration));
//                        }
//                        break;
//                    case 2:
//                        play.setImageResource(R.mipmap.playingplay);
//                        break;
//                    case 1:
//                        play.setImageResource(R.mipmap.playingpause);
//                        break;
//                }
//            }
//        };

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent("GET");
//            intent.putExtra("playposition", playposition);
//            intent.putExtra("position", position);
//            intent.putExtra("isplay", isplay);
//            setResult(2, intent);
            intent.putExtra("control",BACK);
            sendBroadcast(intent);
            finish();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    public void setmusicInfo() {
        rbd = RoundedBitmapDrawableFactory.create(getResources(), mydata.get(position).getPlaycover());
        rbd.setCornerRadius(360);
        playingname.setText(mydata.get(position).getPlayName());
        playingcover.setImageDrawable(rbd);
        if (isplay)
            play.setImageResource(R.mipmap.playingpause);
        else
            play.setImageResource(R.mipmap.playingplay);

        if(playposition!=-1)
            seekBar.setProgress((int) (playposition * seekBar.getMax() / mydata.get(position).getDuration()));

        switch (mode_flag) {
            case 0:
                mode.setImageResource(R.mipmap.mode_order);
                break;
            case 1:
                mode.setImageResource(R.mipmap.mode_circulation);
                break;
            case 2:
                mode.setImageResource(R.mipmap.mode_random);

                break;
            case 3:
                mode.setImageResource(R.mipmap.mode_singlecycle);

                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent("GET");
        switch (v.getId()) {
            case R.id.playingmode:
//                if (mode_flag == 3)
//                    mode_flag = 0;
//                else
//                    mode_flag++;
//                switch (mode_flag) {
//                    case 0:
//                        mode.setImageResource(R.mipmap.mode_order);
//                        break;
//                    case 1:
//                        mode.setImageResource(R.mipmap.mode_circulation);
//                        break;
//                    case 2:
//                        mode.setImageResource(R.mipmap.mode_random);
//
//                        break;
//                    case 3:
//                        mode.setImageResource(R.mipmap.mode_singlecycle);
//
//                        break;
//                }
                intent.putExtra("control", MODE);
                sendBroadcast(intent);
                break;
            case R.id.playingper:
//                switch (mode_flag) {
//                    case 0:
//                    case 1:
//                    case 3:
//                        if (position == 0)
//                            position = mydata.size() - 1;
//                        else
//                            position--;
//                        break;
//                    case 2:
//                        position = r.nextInt(mydata.size());
//                        break;
//                }
//
//                try {
//                    player.reset();
//                    player.setDataSource(mydata.get(position).getUri());
//                    player.prepare();
//                    player.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                setmusicInfo();
//                play.setImageResource(R.mipmap.playingpause);
//                ischoose=true;
//                isplay=true;
                intent.putExtra("control", PREV);
                intent.putExtra("controlplaying", 1234);
                sendBroadcast(intent);
                break;
            case R.id.playingplay:
//                if (isplay) {
//                    isplay = false;
//                    playposition = player.getCurrentPosition();
//                    player.stop();
//                    play.setImageResource(R.mipmap.playingplay);
//                } else {
//                    isplay = true;
//                    player.reset();
//
//                    try {
//                        player.setDataSource(mydata.get(position).getUri());
//
//                        player.prepare();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    player.start();
//                    player.seekTo(playposition);
//                    play.setImageResource(R.mipmap.playingpause);
//                }
//                isfirstcome=false;
//                ischoose=true;

                intent.putExtra("control", PLAY);
                intent.putExtra("controlplaying", 1234);
                sendBroadcast(intent);
                break;
            case R.id.playingnext:
//                switch (mode_flag) {
//                    case 0:
//                    case 1:
//                    case 3:
//                        if (position == mydata.size() - 1)
//                            position = 0;
//                        else
//                            position++;
//                        break;
//                    case 2:
//                        position = r.nextInt(mydata.size());
//                        break;
//                }
//
//                try {
//                    player.reset();
//                    player.setDataSource(mydata.get(position).getUri());
//                    player.prepare();
//                    player.start();
//                    play.setImageResource(R.mipmap.playingpause);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                setmusicInfo();
//                isfirstcome=false;
//                ischoose=true;
//                isplay=true;
                intent.putExtra("control", NEXT);
                intent.putExtra("controlplaying", 1234);
                sendBroadcast(intent);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(fromUser) {
            Intent intent = new Intent("GET");
            intent.putExtra("control", PROGRESSCHANGED);
            intent.putExtra("progress", progress);
            intent.putExtra("fromUser", fromUser);
            intent.putExtra("seekMax", seekBar.getMax());
            sendBroadcast(intent);
            Log.e(TAG, "onProgressChanged");
        }
//
// if(!isplay&&!isfirstcome)
//        {
//            isplay = true;
//            player.reset();
//
//            try {
//                player.setDataSource(mydata.get(position).getUri());
//
//                player.prepare();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            player.start();
//            player.seekTo((int) (float) progress / seekBar.getMax() * player.getDuration());
//            play.setImageResource(R.mipmap.playingpause);
//        }
//        else
//            if(fromUser&&!isfirstcome) {
//                float max = seekBar.getMax();
//                player.seekTo((int) ((float) progress / max * player.getDuration()));
//                Log.e("isfirstcome:",""+isfirstcome);
//            }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "onStartTrackingTouch");

        isfirstcome = false;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "onStopTrackingTouch");
        isfirstcome = false;
    }
}
