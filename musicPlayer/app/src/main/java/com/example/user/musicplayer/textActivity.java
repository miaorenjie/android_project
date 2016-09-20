package com.example.user.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by user on 2016/9/17.
 */
public class textActivity extends Activity {
    private String TAG="222222---------------------------";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        Log.e(TAG, "start onCreate~~~");
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
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Log.e(TAG, "start onKeyDown~~~");
            Intent intent=new Intent();
//            intent.putExtra("playposition",playposition);
//            intent.putExtra("poistion",position);
//            intent.putExtra("isplay",isplay);
            setResult(2,intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
