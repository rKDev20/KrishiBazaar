package com.rk.smsserver;

import android.telephony.SmsManager;

import java.util.ArrayList;

public class SendSms {
    public static void send(String number,String text){
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(text);
        smsManager.sendMultipartTextMessage(number, null, parts,
                null, null);
    }
}
