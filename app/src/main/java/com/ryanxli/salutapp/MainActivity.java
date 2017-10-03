package com.ryanxli.salutapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "SalutApp";
    public Button hostButton;
    public Button clientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hostButton = (Button) findViewById(R.id.host_button);
        clientButton = (Button) findViewById(R.id.client_button);

        hostButton.setOnClickListener(this);
        clientButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(!Salut.isWiFiEnabled(getApplicationContext()))
        {
            Toast.makeText(getApplicationContext(), "Please enable WiFi first.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(v.getId() == R.id.host_button)
        {
            startActivity(new Intent(getApplicationContext(), HostActivity.class));

        }
        else if(v.getId() == R.id.client_button)
        {
            startActivity(new Intent(getApplicationContext(), ClientActivity.class));
        }

    }
}
