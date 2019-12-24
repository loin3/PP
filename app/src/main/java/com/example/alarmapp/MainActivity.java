package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AlarmFragment.OnFragmentInteractionListener {

    AlarmFragment alarmFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmFragment = new AlarmFragment();

        Button button = (Button) findViewById(R.id.alarmFragmentButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, alarmFragment).commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            int hour = data.getExtras().getInt("time hour");
            int minute = data.getExtras().getInt("time minute");

            Bundle bundle = new Bundle();
            bundle.putInt("time hour", hour);
            bundle.putInt("time minute", minute);
            alarmFragment.setArguments(bundle);

            settingAlarm(data.getExtras().getLong("millitime"), hour*100 + minute);
        } else if (resultCode == -1) {
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void settingAlarm(long time, int rawTime){
        ComponentName receiver = new ComponentName(this, AlarmSettingReceiver.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(this, AlarmSettingReceiver.class);
        intent.putExtra("time", rawTime);
        intent.setAction("android.intent.action.BOOT_COMPLETED");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, rawTime, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}

