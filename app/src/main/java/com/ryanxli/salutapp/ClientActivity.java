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
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.io.IOException;
import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity implements SalutDataCallback, View.OnClickListener {

    public static final String TAG = "SalutApp";
    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public Salut salut;

    public ListView deviceListView;
    public ArrayAdapter deviceArrayAdapter;

    public Button sendButton;
    public EditText editText;
    public ListView messageListView;
    public ArrayAdapter<String> messageArrayAdapter;
    public ArrayList<String> messageArrayList;
    public ArrayList<String> deviceArrayList;

    public static int trials = 10;
    public static int trialSize = 1024 * 2; // in KB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        deviceListView = (ListView) findViewById(R.id.client_listView);


        dataReceiver = new SalutDataReceiver(this, this);
        serviceData = new SalutServiceData("myService", 6666, android.os.Build.MODEL);

        salut = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });

        deviceArrayList = new ArrayList<>();

        deviceArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.device_adapter, deviceArrayList);
        deviceListView.setAdapter(deviceArrayAdapter);

        salut.discoverNetworkServices(new SalutCallback() {
            @Override
            public void call() {
                deviceArrayList.add(0, salut.foundDevices.get(0).instanceName);
                deviceArrayAdapter.notifyDataSetChanged();
                Log.d(TAG, "Device: " + salut.foundDevices.get(0).instanceName + " found.");
                Toast.makeText(getApplicationContext(), "Device: " + salut.foundDevices.get(0).instanceName + " found.", Toast.LENGTH_SHORT).show();
            }
        }, true);


        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String deviceName = deviceArrayList.get(position);
                Boolean found = false;

                for (SalutDevice device : salut.foundDevices) {
                    if (device.instanceName.equals(deviceName)) {
                        found = true;
                        salut.registerWithHost(device, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.d(TAG, "We're now registered.");

                            }
                        }, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.d(TAG, "We failed to register.");
                            }
                        });
                        changeContentView();
                    }
                    if (!found) {
                        Toast.makeText(getApplicationContext(), "Device" + deviceName + "is not not available.", Toast.LENGTH_SHORT).show();
                        deviceArrayList.remove(position);
                    }
                }

            }
        });
    }


    public void changeContentView() {
        setContentView(R.layout.activity_chat);

        sendButton = (Button) findViewById(R.id.chat_send);
        editText = (EditText) findViewById(R.id.chat_editText);
        messageListView = (ListView) findViewById(R.id.chat_listView);

        sendButton.setOnClickListener(this);
        messageArrayList = new ArrayList<>();
        messageArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.device_adapter, messageArrayList);
        messageListView.setAdapter(messageArrayAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        salut.unregisterClient(false);
    }

    @Override
    public void onDataReceived(Object data) {
        Log.d(TAG, "Received network data.");
        try
        {
            Message newMessage = LoganSquare.parse((String) data, Message.class);
            Log.d(TAG, newMessage.sender);
            Log.d(TAG, newMessage.content);
            messageArrayList.add(newMessage.sender + ": " + newMessage.content);
            messageArrayAdapter.notifyDataSetChanged();
            messageListView.smoothScrollToPosition(messageArrayList.size() - 1);
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Failed to parse network data.");
        }

//        try
//        {
//            Message newMessage = LoganSquare.parse((String) data, Message.class);
//            newMessage.receiveTime = System.currentTimeMillis();
//            String speed = Long.toString((ClientActivity.trialSize * 1000)/(newMessage.receiveTime - newMessage.sendTime));
//
//            Log.d(TAG, newMessage.sender + ": " + speed + " kbps");
//
//
//            messageArrayList.add(newMessage.sender + ": " + speed + " kbps");
//            // in kbps
//
//            Log.d(TAG, "added.");
//            messageArrayAdapter.notifyDataSetChanged();
//            messageListView.smoothScrollToPosition(messageArrayList.size() - 1);
//        }
//        catch (IOException ex)
//        {
//            Log.e(TAG, "Failed to parse network data.");
//        }
    }

    @Override
    public void onClick(View v) {
        String content = editText.getText().toString();
        if (!"".equals(content)) {
            Message toSend = new Message();
            toSend.content = content;
            toSend.sender = android.os.Build.MODEL + " (client)";
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


//
////        for (int i = 0; i < trials; i++) {
//            Message toSend = new Message();
//            toSend.content = createKBData(trialSize);
//            toSend.sender = android.os.Build.MODEL + " (client)";
//            toSend.sendTime = System.currentTimeMillis();
//            salut.sendToHost(toSend, new SalutCallback() {
//                @Override
//                public void call() {
//                    Log.e(TAG, "The data failed to send.");
//                }
//            });
//
////            try
////            {
////                Thread.sleep(1000);
////            }
////            catch(InterruptedException ex)
////            {
////                Thread.currentThread().interrupt();
////            }
////        }
        }
    }
}

//    public static String createKBData(int size) {
////        int[] ints = new int[1024 / Integer.SIZE * size];
////        return ints;
//        int arraySize = 1024 / Character.SIZE * size;
//        char[] chars = new char[arraySize];
////        for (int i = 0; i < arraySize; i++) {
////            chars[i] = '0';
////        }
//        return new String(chars);
//    }
//}
