package com.rk.smsserver;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import static com.rk.smsserver.SharedPreferenceManager.CHANNEL_BOT;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Switch actionServer;
    Switch botServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        actionServer = findViewById(R.id.action_server);
        botServer = findViewById(R.id.bot_server);
        actionServer.setOnCheckedChangeListener(this);
        botServer.setOnCheckedChangeListener(this);
        int channel = SharedPreferenceManager.getChannel(this);
        if (channel != SharedPreferenceManager.CHANNEL_NONE) {
            if (channel == CHANNEL_BOT)
                botServer.setChecked(true);
            else
                actionServer.setChecked(true);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == actionServer) {
            if (isChecked) {
                SharedPreferenceManager.setChannel(this, SharedPreferenceManager.CHANNEL_ACTION);
                FirebaseMessaging.getInstance().subscribeToTopic("all");
                toggleSms(true);
                botServer.setChecked(false);
            } else {
                if (!botServer.isChecked()) {
                    SharedPreferenceManager.setChannel(this, SharedPreferenceManager.CHANNEL_NONE);
                    toggleSms(false);
                }
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
            }
        } else {
            if (isChecked) {
                SharedPreferenceManager.setChannel(this, CHANNEL_BOT);
                toggleSms(true);
                actionServer.setChecked(false);
            } else {
                if (!actionServer.isChecked()) {
                    SharedPreferenceManager.setChannel(this, SharedPreferenceManager.CHANNEL_NONE);
                    toggleSms(false);
                }
            }
        }
    }

    private void toggleSms(boolean enable) {
        PackageManager pm = getApplicationContext().getPackageManager();
        ComponentName componentName = new ComponentName(this, SmsBroadcastReceiver.class);
        int state = enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        pm.setComponentEnabledSetting(componentName, state, PackageManager.DONT_KILL_APP);

    }
}
