package com.rk.smsserver;

import android.content.Context;
import android.content.SharedPreferences;

class SharedPreferenceManager {
    static final int CHANNEL_BOT = 1;
    static final int CHANNEL_ACTION = 2;
    static final int CHANNEL_NONE = 0;

    static int getChannel(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("default", 0);
        return preferences.getInt("channel", CHANNEL_NONE);
    }

    static void setChannel(Context context, int channel) {
        SharedPreferences preferences = context.getSharedPreferences("default", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("channel", channel);
        editor.apply();
    }
}
