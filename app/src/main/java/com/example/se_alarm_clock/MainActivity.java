package com.example.se_alarm_clock;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private LinearLayout llayout;
    private Button setAlarm;
    private int toggle,hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        timePicker = findViewById(R.id.timepicker);
        setAlarm = findViewById(R.id.setBtn);
        llayout = findViewById(R.id.llayout);
        toggle = 0;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggle == 0){
                    timePicker.setVisibility(View.VISIBLE);
                    setAlarm.setVisibility(View.VISIBLE);
                    toggle++;
                }
                else if (toggle == 1){
                    timePicker.setVisibility(View.GONE);
                    setAlarm.setVisibility(View.GONE);
                    toggle--;
                }

            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();

                String time_set = hour + ":" + minute;
                alarmTime(time_set);
            }
        });

    }

    public void alarmTime(String newT){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View view1 = inflater.inflate(R.layout.alarm_set, llayout, false);
        final TextView alarm_time = view1.findViewById(R.id.time);

        alarm_time.setText(newT);

        llayout.addView(view1);
    }



}
