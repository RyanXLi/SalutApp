package com.ryanxli.salutapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "SalutApp";
    public Button hostButton;
    public Button clientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hostButton = (Button) findViewById(R.id.host_button);
        clientButton = (Button) findViewById(R.id.client_button);

        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HostActivity.class));
            }
        });
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ClientActivity.class));
            }
        });

//         /*Create a data receiver object that will bind the callback
//        with some instantiated object from our app. */
//        dataReceiver = new SalutDataReceiver(this, this);
//        /*Populate the details for our awesome service. */
//        serviceData = new SalutServiceData("testAwesomeService", 60606,
//                "HOST");
//        /*Create an instance of the Salut class, with all of the necessary data from before.
//        * We'll also provide a callback just in case a device doesn't support WiFi Direct, which
//        * Salut will tell us about before we start trying to use methods.*/
//        network = new MySalut(dataReceiver, serviceData, new SalutCallback() {
//            @Override
//            public void call() {
//                // wiFiFailureDiag.show();
//                // OR
//                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
//
//
//            }
//        });
    }


}
