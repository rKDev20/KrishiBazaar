package com.rk.smsserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        Log.d("abcd","received");
        if (intentExtras != null) {
            SmsMessage[] smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            String smsMessageStr = "";
            for (SmsMessage message : smsMessage) {
                String smsBody = message.getMessageBody();
                String address = message.getOriginatingAddress();
                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
                smsMessageStr += message.getDisplayMessageBody();
                smsMessageStr += message.getDisplayOriginatingAddress();
                smsMessageStr += message.getEmailBody();
                smsMessageStr += message.getEmailFrom();
                Log.d("abcd", smsMessageStr);
            }
        }
    }
}