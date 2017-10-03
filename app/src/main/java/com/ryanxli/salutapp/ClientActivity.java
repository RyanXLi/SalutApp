package com.ryanxli.salutapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

public class ClientActivity extends AppCompatActivity implements SalutDataCallback {

    public static final String TAG = "SalutApp";
    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public MySalut salut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        dataReceiver = new SalutDataReceiver(this, this);
        serviceData = new SalutServiceData("myService", 50489, android.os.Build.MODEL);

        salut = new MySalut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });

        salut.discoverNetworkServices(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice device) {
                Log.d(TAG, "A device has connected with the name " + device.deviceName);
            }
        }, false);
    }

    @Override
    public void onDataReceived(Object o) {

    }
}
