package com.ryanxli.salutapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.peak.salut.Callbacks.SalutDataCallback;

public class ClientChatActivity extends AppCompatActivity implements SalutDataCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void onDataReceived(Object o) {

    }
}
