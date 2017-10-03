package com.ryanxli.salutapp;

/**
 * Created by Ryan on 2017/10/2.
 */

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Message {
    /*
 * Annotate a field that you want sent with the @JsonField marker.
 */
    @JsonField
    public String content;

    @JsonField
    public String sender;

}
