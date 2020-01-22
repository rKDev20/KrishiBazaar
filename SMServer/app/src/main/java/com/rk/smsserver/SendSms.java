package com.rk.smsserver;

import android.telephony.SmsManager;

public class SendSms {
    public static void send(String number,String text){
        SmsManager manager=SmsManager.getDefault();
        manager.sendTextMessage(number,null,text,null,null);
    }
}
