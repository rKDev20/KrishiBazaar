package com.rk.smsserver;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Switch sendSms;
    Switch botServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        sendSms = findViewById(R.id.send_sms);
        botServer = findViewById(R.id.bot_server);
        sendSms.setOnCheckedChangeListener(this);
        botServer.setOnCheckedChangeListener(this);
        sendSms.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("SEND_SMS",true));
        botServer.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("BOT_SERVER",true));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == sendSms) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean("SEND_SMS", isChecked);
            editor.apply();
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("all");
            } else FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        } else {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean("BOT_SERVER", isChecked);
            editor.apply();
            PackageManager pm = getApplicationContext().getPackageManager();
            ComponentName componentName = new ComponentName(this, SmsBroadcastReceiver.class);
            int state = isChecked ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
            pm.setComponentEnabledSetting(componentName, state, PackageManager.DONT_KILL_APP);
        }
    }
}
