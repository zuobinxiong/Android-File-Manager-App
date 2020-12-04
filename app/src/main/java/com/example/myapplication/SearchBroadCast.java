package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SearchBroadCast extends BroadcastReceiver {
    public static String mServiceKeyword = "";
    public static String mServiceSearchPath = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        String mAction = intent.getAction();
        if (MainActivity.KEYWORD_BROADCAST.equals(mAction)) {

            mServiceKeyword = intent.getStringExtra("keyword");
            mServiceSearchPath = intent.getStringExtra("searchpath");
        }
    }
}
