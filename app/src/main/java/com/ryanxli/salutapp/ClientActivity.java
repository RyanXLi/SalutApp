package com.ryanxli.salutapp;

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

public class ClientActivity extends AppCompatActivity implements SalutDataCallback, View.OnClickListener {

    public static final String TAG = "SalutApp";
    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public MySalut salut;

    public ListView deviceListView;
    public ArrayAdapter deviceArrayAdapter;

    public Button sendButton;
    public EditText editText;
    public ListView messageListView;
    public ArrayAdapter<String> messageArrayAdapter;
    public ArrayList<String> messageArrayList;

    public ClientActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        that = this;

        deviceListView = (ListView) findViewById(R.id.client_listView);


        dataReceiver = new SalutDataReceiver(this, this);
        serviceData = new SalutServiceData("myService", 50489, android.os.Build.MODEL);

        salut = new MySalut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });


        deviceArrayAdapter = new ArrayAdapter<SalutDevice>(getApplicationContext(), R.layout.device_adapter, salut.foundDevices);
        deviceListView.setAdapter(deviceArrayAdapter);

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setContentView(R.layout.activity_chat);

                sendButton = (Button) findViewById(R.id.chat_send);
                editText = (EditText) findViewById(R.id.chat_editText);
                messageListView = (ListView) findViewById(R.id.chat_listView);

                sendButton.setOnClickListener(that);

                messageArrayList = new ArrayList<>();
                messageArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.device_adapter, messageArrayList);
                messageListView.setAdapter(messageArrayAdapter);

                salut.discoverNetworkServices(new SalutCallback() {
                    @Override
                    public void call() {
                        messageArrayAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Device: " + salut.foundDevices.get(0).instanceName + " found.");
                        Toast.makeText(getApplicationContext(), "Device: " + salut.foundDevices.get(0).instanceName + " found.", Toast.LENGTH_SHORT).show();
                    }
                }, true);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        salut.stopServiceDiscovery(true);
        salut.unregisterClient(true);
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
            messageArrayAdapter.notifyDataSetChanged();
            messageListView.smoothScrollToPosition(messageArrayList.size() - 1);
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
            salut.sendToHost(toSend, new SalutCallback() {
                @Override
                public void call() {
                    Log.e(TAG, "The data failed to send.");
                }
            });
            messageArrayList.add(toSend.sender + ": " + toSend.content);
            messageArrayAdapter.notifyDataSetChanged();
            messageListView.smoothScrollToPosition(messageArrayList.size() - 1);
            editText.setText("");
        }
    }
}
