package com.example.eyesaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener , View.OnClickListener  {
    public int HClamper (int N) {
        if (N > 23) {
            N = N - 24;
        } else if (N < 0) {
            N = 24 + N;
        }
        return N;
    }
    public int MClamper (int N) {
        if (N > 59) {
            N = N - 59;
        } else if (N < 1) {
            N = 59 + N;
        }
        return N;
    }
    public void SetWorkTimerText (int HT,int MT) {
        WHS1.setText(String.valueOf(HClamper(HT-1)));
        WHS2.setText(String.valueOf(HT));
        WHS3.setText(String.valueOf(HClamper(HT+1)));
        WMS1.setText(String.valueOf(MClamper(MT-1)));
        WMS2.setText(String.valueOf(MT));
        WMS3.setText(String.valueOf(MClamper(MT+1)));
    }
    public void SetEyesTimerText (int HT,int MT) {
        EHS1.setText(String.valueOf(HClamper(HT-1)));
        EHS2.setText(String.valueOf(HT));
        EHS3.setText(String.valueOf(HClamper(HT+1)));
        EMS1.setText(String.valueOf(MClamper(MT-1)));
        EMS2.setText(String.valueOf(MT));
        EMS3.setText(String.valueOf(MClamper(MT+1)));
    }
    public void GetWorkSavedTime () {
        sPref = getSharedPreferences("settings",MODE_PRIVATE);
        savedTime = sPref.getInt(WORK_SAVED_TIME_H,0);
        WHSTime = savedTime;
        savedTime = sPref.getInt(WORK_SAVED_TIME_M,45);
        WMSTime = savedTime;
    }
    public void GetEyesSavedTime () {
        sPref = getSharedPreferences("settings",MODE_PRIVATE);
        savedTime = sPref.getInt(EYES_SAVED_TIME_H,0);
        EHSTime = savedTime;
        savedTime = sPref.getInt(EYES_SAVED_TIME_M,15);
        EMSTime = savedTime;
    }
    public void StartTimer (int HT, int MT) {
            if (WTimerState) {
                //Work timer
                TimerTime = HT * 3600000 + MT * 60000;
                tmr = new CountDownTimer(TimerTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (lcdState) {
                            WorkLCD.setText("");
                            lcdState = false;
                        } else {
                            WorkLCD.setText(":");
                            lcdState = true;
                        }
                        HRunTime = (int) ((millisUntilFinished / 1000) / 3600);
                        MRunTime = (int) (((millisUntilFinished / 1000) / 60) % 60);
                        SetWorkTimerText(HRunTime, MRunTime);
                    }

                    @Override
                    public void onFinish() {
                        WorkLCD.setText(":");
                        WTimerState = false;
                        Intent intent = new Intent(MainActivity.this, StopActivity.class);
                        intent.putExtra("WTimer",true);
                        startActivity(intent);
                        TimerState = true;
                    }
                }.start();
            } else {
                //Eyes timer
                TimerTime = HT * 3600000 + MT * 60000;
                tmr = new CountDownTimer(TimerTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (lcdState) {
                            EyesLCD.setText("");
                            lcdState = false;
                        } else {
                            EyesLCD.setText(":");
                            lcdState = true;
                        }
                        HRunTime = (int) ((millisUntilFinished / 1000) / 3600);
                        MRunTime = (int) (((millisUntilFinished / 1000) / 60) % 60);
                        SetEyesTimerText(HRunTime, MRunTime);
                    }

                    @Override
                    public void onFinish() {
                        EyesLCD.setText(":");
                        WTimerState = true;
                        Intent intent = new Intent(MainActivity.this, StopActivity.class);
                        intent.putExtra("WTimer",false);
                        startActivity(intent);
                        TimerState = true;
                    }
                }.start();
            }
    }
    public void AcceptTimer () {
        GetWorkSavedTime();
        SetWorkTimerText(WHSTime,WMSTime);
        GetEyesSavedTime();
        SetEyesTimerText(WHSTime,WMSTime);
        StartTimer(WHSTime,WMSTime);
    }
    public static MainActivity current;

    public TextView WorkLCD;
    public TextView EyesLCD;
    boolean lcdState = true;

    public static SharedPreferences sPref;
    int savedTime;
    final String WORK_SAVED_TIME_H = "work_saved_time_h";
    final String WORK_SAVED_TIME_M = "work_saved_time_m";
    final String EYES_SAVED_TIME_H = "eyes_saved_time_h";
    final String EYES_SAVED_TIME_M = "eyes_saved_time_m";
    public CountDownTimer tmr;
    public boolean WTimerState = true;
    public boolean TimerState = false;
    int HRunTime;
    int MRunTime;

    int StartY;
    int y;
    int Result;

    int WHSTime;
    int WMSTime;
    int EHSTime;
    int EMSTime;

    long TimerTime;

    TextView WHS1;
    TextView WHS2;
    TextView WHS3;
    TextView WMS1;
    TextView WMS2;
    TextView WMS3;

    TextView EHS1;
    TextView EHS2;
    TextView EHS3;
    TextView EMS1;
    TextView EMS2;
    TextView EMS3;

    ImageView SSButt;

    public void onClick(View v) {
        if(TimerState){
            tmr.cancel();
            WorkLCD.setText(":");
            EyesLCD.setText(":");
            WTimerState = true;
            GetWorkSavedTime();
            SetWorkTimerText(WHSTime,WMSTime);
            GetEyesSavedTime();
            SetEyesTimerText(EHSTime,EMSTime);
            SSButt.setImageResource(R.drawable.icon_start1);
        } else {
            SharedPreferences.Editor ed = sPref.edit();
            ed.putInt(WORK_SAVED_TIME_H,WHSTime);
            ed.putInt(WORK_SAVED_TIME_M,WMSTime);
            ed.putInt(EYES_SAVED_TIME_H,EHSTime);
            ed.putInt(EYES_SAVED_TIME_M,EMSTime);
            ed.apply();
            Toast.makeText(this, "Time saved", Toast.LENGTH_SHORT).show();
            SSButt.setImageResource(R.drawable.icon_stop1);
            StartTimer(WHSTime,WMSTime);
        }
        TimerState=!TimerState;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current = this;

        WHS1 = findViewById(R.id.WorkHoursSelector1);
        WHS2 = findViewById(R.id.WorkHoursSelector2);
        WHS3 = findViewById(R.id.WorkHoursSelector3);
        WMS1 = findViewById(R.id.WorkMinutesSelector1);
        WMS2 = findViewById(R.id.WorkMinutesSelector2);
        WMS3 = findViewById(R.id.WorkMinutesSelector3);

        EHS1 = findViewById(R.id.EyesHoursSelector1);
        EHS2 = findViewById(R.id.EyesHoursSelector2);
        EHS3 = findViewById(R.id.EyesHoursSelector3);
        EMS1 = findViewById(R.id.EyesMinutesSelector1);
        EMS2 = findViewById(R.id.EyesMinutesSelector2);
        EMS3 = findViewById(R.id.EyesMinutesSelector3);

        SSButt = findViewById(R.id.StartAndStopImgButton);

        WorkLCD = findViewById(R.id.WorkLCD);
        EyesLCD = findViewById(R.id.EyesLCD);

        GetWorkSavedTime();
        SetWorkTimerText(WHSTime,WMSTime);
        GetEyesSavedTime();
        SetEyesTimerText(EHSTime,EMSTime);

        SSButt.setOnClickListener(this);

        WHS1.setOnTouchListener(this);
        WHS2.setOnTouchListener(this);
        WHS3.setOnTouchListener(this);
        WMS1.setOnTouchListener(this);
        WMS2.setOnTouchListener(this);
        WMS3.setOnTouchListener(this);

        EHS1.setOnTouchListener(this);
        EHS2.setOnTouchListener(this);
        EHS3.setOnTouchListener(this);
        EMS1.setOnTouchListener(this);
        EMS2.setOnTouchListener(this);
        EMS3.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!TimerState) {
            if (v.getId() == R.id.WorkHoursSelector1 || v.getId() == R.id.WorkHoursSelector2 || v.getId() == R.id.WorkHoursSelector3) {
                y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {// нажатие
                        StartY = y;
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {// движение
                        Result = WHSTime + (StartY - y) / 90;
                        WHS1.setText(String.valueOf(HClamper(Result - 1)));
                        WHS2.setText(String.valueOf(HClamper(Result)));
                        WHS3.setText(String.valueOf(HClamper(Result + 1)));
                    }
                    break;
                    case MotionEvent.ACTION_UP: { // отпускание
                        WHSTime = HClamper(WHSTime + (StartY - y) / 90);
                    }
                    break;

                }

            }
            if (v.getId() == R.id.WorkMinutesSelector1 || v.getId() == R.id.WorkMinutesSelector2 || v.getId() == R.id.WorkMinutesSelector3) {
                y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {// нажатие
                        StartY = y;
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {// движение
                        Result = WMSTime + (StartY - y) / 50;
                        WMS1.setText(String.valueOf(MClamper(Result - 1)));
                        WMS2.setText(String.valueOf(MClamper(Result)));
                        WMS3.setText(String.valueOf(MClamper(Result + 1)));
                    }
                    break;
                    case MotionEvent.ACTION_UP: { // отпускание
                        WMSTime = MClamper(WMSTime + (StartY - y) / 50);
                    }
                    break;

                }

            }

            if (v.getId() == R.id.EyesHoursSelector1 || v.getId() == R.id.EyesHoursSelector2 || v.getId() == R.id.EyesHoursSelector3) {
                y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {// нажатие
                        StartY = y;
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {// движение
                        Result = EHSTime + (StartY - y) / 90;
                        EHS1.setText(String.valueOf(HClamper(Result - 1)));
                        EHS2.setText(String.valueOf(HClamper(Result)));
                        EHS3.setText(String.valueOf(HClamper(Result + 1)));
                    }
                    break;
                    case MotionEvent.ACTION_UP: { // отпускание
                        EHSTime = HClamper(EHSTime + (StartY - y) / 90);
                    }
                    break;

                }

            }
            if (v.getId() == R.id.EyesMinutesSelector1 || v.getId() == R.id.EyesMinutesSelector2 || v.getId() == R.id.EyesMinutesSelector3) {
                y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {// нажатие
                        StartY = y;
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {// движение
                        Result = EMSTime + (StartY - y) / 50;
                        EMS1.setText(String.valueOf(MClamper(Result - 1)));
                        EMS2.setText(String.valueOf(MClamper(Result)));
                        EMS3.setText(String.valueOf(MClamper(Result + 1)));
                    }
                    break;
                    case MotionEvent.ACTION_UP: { // отпускание
                        EMSTime = MClamper(EMSTime + (StartY - y) / 50);
                    }
                    break;

                }

            }

        }
        return true;
    }
}