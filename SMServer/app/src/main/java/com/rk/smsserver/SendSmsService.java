package com.rk.smsserver;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.rk.smsserver.Constants.MOBILE;
import static com.rk.smsserver.Constants.TEXT;

public class SendSmsService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("abcd","here");
        SendSms.send(remoteMessage.getData().get(MOBILE), remoteMessage.getData().get(TEXT));
    }

    @Override
    public void onNewToken(String s) {
        FirebaseMessaging id = FirebaseMessaging.getInstance();
        id.subscribeToTopic("all");
    }
}
