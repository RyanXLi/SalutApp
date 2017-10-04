package com.ryanxli.salutapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.data;

public class HostActivity extends AppCompatActivity implements SalutDataCallback, View.OnClickListener {

    public static final String TAG = "SalutApp";
    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public MySalut salut;
    public Button sendButton;
    public EditText editText;
    public ListView listView;
    public ArrayAdapter<String> arrayAdapter;
    public ArrayList<String> messageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendButton = (Button) findViewById(R.id.chat_send);
        editText = (EditText) findViewById(R.id.chat_editText);
        listView = (ListView) findViewById(R.id.chat_listView);
        sendButton.setOnClickListener(this);

        messageArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.device_adapter, messageArrayList);
        listView.setAdapter(arrayAdapter);


        dataReceiver = new SalutDataReceiver(this, this);
        serviceData = new SalutServiceData("myService", 6666, android.os.Build.MODEL);

        salut = new MySalut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });

        salut.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice device) {
                Log.d(TAG, device.readableName + " has connected!");
                // TODO: put into listView
            }
        });

        salut.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice salutDevice) {
                Toast.makeText(getApplicationContext(), "Device: " + salutDevice.instanceName + " connected.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        salut.stopServiceDiscovery(true);
        salut.stopNetworkService(false);
    }

    @Override
    public void onDataReceived(Object data) {
        Log.d(TAG, "Received network data.");
        try
        {
            Message newMessage = LoganSquare.parse((String) data, Message.class);
            Log.d(TAG, newMessage.sender);
            Log.d(TAG, newMessage.content);
            messageArrayList.add(newMessage.sender + ": " + newMessage);
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Failed to parse network data.");
        }
    }

    @Override
    public void onClick(View v) {
        String content = editText.getText().toString();
        if (!"".equals(content)) {
            Message toSend = new Message();
            toSend.content = content;
            toSend.sender = android.os.Build.MODEL + " (host)";
            salut.sendToAllDevices(toSend, new SalutCallback() {
                @Override
                public void call() {
                    Log.e(TAG, "The data failed to send.");
                }
            });
            messageArrayList.add(toSend.sender + ": " + toSend.content);
            arrayAdapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(messageArrayList.size() - 1);
            editText.setText("");
        }
    }
}

