package com.example.eyesaver;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.CountDownTimer;
import android.widget.TextView;

public class StopActivity extends Activity implements View.OnClickListener{
    Button Btnn;
    TextView Text;

    public MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_timer_button);
        Btnn = findViewById(R.id.AcceptTimer);
        Text = findViewById(R.id.TimeIsUpText);

        Btnn.setOnClickListener(this);
        player = MediaPlayer.create(this,R.raw.simple_marimba);
        player.setLooping(true);
        player.start();

        Bundle arguments = getIntent().getExtras();
        if(arguments.getBoolean("WTimer")) {
            Text.setText("It's time to distract from the screen");
        } else {
            Text.setText("Time to get back to work");
        }

        new CountDownTimer(30000,30000) {
            @Override
            public void onTick(long i) {}
            @Override
            public void onFinish() {finish();}
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        MainActivity.current.AcceptTimer();
    }

    public void onClick(View view) {
        finish();
    }
}
