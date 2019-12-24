package com.example.alarmapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimeSettingActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        timePicker = findViewById(R.id.time_picker);
        final Calendar calendar = Calendar.getInstance();


        final Button alarmOn = findViewById(R.id.button_start);
        alarmOn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());

                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("time hour", hour);
                intent.putExtra("time minute", minute);
                setResult(0, intent);

                finish();
            }
        });

        final Button alarmOff = findViewById(R.id.button_end);
        alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(-1);
                finish();
            }
        });
    }
}