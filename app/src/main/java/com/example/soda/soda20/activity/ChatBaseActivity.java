package com.example.soda.soda20.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.soda.soda20.broad_rec.CallBroadCaskReceiver;
import com.hyphenate.chat.EMClient;

import butterknife.ButterKnife;


/**
 * Created by soda on 2017/8/7.
 */

public abstract class ChatBaseActivity extends AppCompatActivity {
    protected CallBroadCaskReceiver callBroadCaskReceiver;
    protected IntentFilter callFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        ButterKnife.bind(this);
        loadData();
    }

    protected abstract void loadData();

    protected abstract void initLayout();

    @Override
    protected void onResume() {
        super.onResume();
        callBroadCaskReceiver = new CallBroadCaskReceiver();
        callFilter = new IntentFilter();
        callFilter.addAction(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(callBroadCaskReceiver,callFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(callBroadCaskReceiver);
    }
}
