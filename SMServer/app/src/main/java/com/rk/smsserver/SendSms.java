package com.rk.smsserver;

import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

class SendSms {
    static void send(String number, String text){
        Log.d("abcd",number+text);
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(text);
        smsManager.sendMultipartTextMessage(number, null, parts,
                null, null);
    }
}
