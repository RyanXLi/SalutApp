package com.ryanxli.salutapp;

/**
 * Created by Ryan on 2017/10/2.
 */

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;


public class MySalut extends Salut {
    public MySalut(SalutDataReceiver dataReceiver, SalutServiceData salutServiceData, SalutCallback deviceNotSupported) {
        super(dataReceiver, salutServiceData, deviceNotSupported);
    }

    @Override
    public String serialize(Object o) {
        return null;
    }
}