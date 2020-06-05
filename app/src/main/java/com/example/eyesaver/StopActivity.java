package com.example.eyesaver;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.os.CountDownTimer;

public class StopActivity extends Activity implements View.OnClickListener{
    Button Btnn;

    //public CountDownTimer tmr;

    public MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*tmr = new CountDownTimer(300,300) {
            @Override
            public void onTick(long i) {}
            @Override
            public void onFinish() {
                player.stop();
            }
        }.start(); */

        setContentView(R.layout.stop_timer_button);

        Btnn = findViewById(R.id.AcceptTimer);
        Btnn.setOnClickListener(this);
        /*player.setLooping(true);
        player = MediaPlayer.create(this,R.raw.alarm_classic);
        player.start(); */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //player.stop();
        MainActivity.current.AcceptTimer();
    }

    public void onClick(View view) {
        finish();
    }
}
