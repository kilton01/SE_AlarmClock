package com.example.se_alarm_clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private LinearLayout llayout,linearLayout;
    private Button setAlarm,cancel,snoozeBtn,setSnoozeBtn,editSnooze;
    private String hour,minute;
    private AlertDialog.Builder alert;
    private AlertDialog dialog;
    private Switch timeSwitch;
    private TextClock clock,clock1;
    private RingtoneManager ringtoneManager;
    private static Ringtone ringtone;
    private EditText setSnoozeTime;
    private int snoozeTime = 2;
    private RelativeLayout snoozeLayer;
    private TextView alarm_time, alarm_time12,RemoveNoAlarm;
    private boolean toggle = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ringtoneManager = new RingtoneManager(getApplicationContext());
        FloatingActionButton fab = findViewById(R.id.fab);
        timeSwitch = findViewById(R.id.switch1);
        clock = findViewById(R.id.dclock);
        clock1 = findViewById(R.id.dclock1);

        llayout = findViewById(R.id.llayout);
        linearLayout = findViewById(R.id.linearlayout);
        RemoveNoAlarm = findViewById(R.id.noAlarm);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();

            }
        });

        timeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSwitch.isChecked()){
                    clock.setVisibility(View.GONE);
                    clock1.setVisibility(View.VISIBLE);

                    alarm_time12.setVisibility(View.GONE);
                    alarm_time.setVisibility(View.VISIBLE);
                }else{
                    clock.setVisibility(View.VISIBLE);
                    clock1.setVisibility(View.GONE);

                    alarm_time.setVisibility(View.GONE);
                    alarm_time12.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void alarmTime(String newT){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View view1 = inflater.inflate(R.layout.alarm_set, llayout, false);
        alarm_time = view1.findViewById(R.id.time);
        alarm_time12 = view1.findViewById(R.id.time1);
        cancel = view1.findViewById(R.id.cancelBtn);
        snoozeBtn = view1.findViewById(R.id.snooze);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRing();
            }
        });

        snoozeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRing();
                snoozeAlarm();
            }
        });

            alarm_time.setText(newT); // 24 hour display

            String am_pm;
            alarm_time.setVisibility(View.GONE);
            alarm_time12.setVisibility(View.VISIBLE);

            String[] parts = newT.split(":");
            String Hour = parts[0];
            String Minutes = parts[1];

            if (Integer.parseInt(Hour) > 12){
                am_pm = "PM";
                int hr = Integer.parseInt(Hour) - 12;
                Hour = String.valueOf(hr);
            }else{
                am_pm = "AM";
            }
            String newTin12 = Hour+":"+Minutes +" "+ am_pm;
            alarm_time12.setText(newTin12); //12 hour display



        llayout.addView(view1);
    }

    public void addAlarm(){
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View view = layoutInflater.inflate(R.layout.add_alarm,linearLayout, false);

        timePicker = view.findViewById(R.id.timepicker);
        timePicker.setIs24HourView(timeSwitch.isChecked());
        setAlarm = view.findViewById(R.id.setBtn);
        setSnoozeTime = view.findViewById(R.id.inputSnoozeTime);
        snoozeLayer = view.findViewById(R.id.snoozeLayout);
        setSnoozeBtn = view.findViewById(R.id.Ssnooze);
        editSnooze = view.findViewById(R.id.setSnoozeOpt);


        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                hour = String.valueOf(timePicker.getCurrentHour());
                minute = String.valueOf(timePicker.getCurrentMinute());
//                if(minute.length() == 1){
//                    minute = "0"+minute;
//                }
                minute = minute.length() == 1 ? '0'+minute : minute;
                hour = hour.length() == 1 ? '0'+hour : hour;

                RemoveNoAlarm.setVisibility(View.GONE);
                alarmTime(hour + ":" + minute);
                executingAlarm(hour,minute);
                dialog.dismiss();
            }
        });

        editSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggle){
                    snoozeLayer.setVisibility(View.VISIBLE);
                    toggle = false;
                }else{
                    snoozeLayer.setVisibility(View.GONE);
                    toggle = true;
                }

            }
        });

        setSnoozeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snoozeTime = Integer.parseInt(setSnoozeTime.getText().toString());
                Toast.makeText(MainActivity.this, "SNOOZE TIME ADJUSTED", Toast.LENGTH_SHORT).show();
                snoozeLayer.setVisibility(View.GONE);
            }
        });


        alert  = new AlertDialog.Builder(MainActivity.this);
        alert.setView(view);
        dialog = alert.create();
        dialog.show();

    }

    public void executingAlarm(String hour, String minute){

        int Hour = Integer.parseInt(hour);
        int Minute = Integer.parseInt(minute);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this,0,
                intent,0);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar startTime = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        startTime.set(Calendar.HOUR_OF_DAY, Hour);
        startTime.set(Calendar.MINUTE, Minute);
        startTime.set(Calendar.SECOND,0);
        long alarmStartTime = startTime.getTimeInMillis();

        alarm.setExact(AlarmManager.RTC_WAKEUP,alarmStartTime,alarmIntent);
        startRing();

        Toast.makeText(this, "ALARM SET!", Toast.LENGTH_SHORT).show();


    }

    public Ringtone startRing(){
        Uri alarmUri = ringtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null){
            alarmUri = ringtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = ringtoneManager.getRingtone(getApplicationContext(),alarmUri);

        return ringtone;
//        ringtone.play();
    }

    public static Ringtone getRing(){
        Ringtone ringNow = ringtone;
        return ringNow;
    }

    public void stopRing(){
        ringtone.stop();
    }

    public void snoozeAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent,0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + snoozeTime * 60000, pendingIntent1); //2 minutes snooze time
        startRing();

        Toast.makeText(this, "ALARM SNOOZED!", Toast.LENGTH_SHORT).show();

    }
}


