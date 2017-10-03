package com.ryanxli.salutapp;

/**
 * Created by Ryan on 2017/10/2.
 */

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by RyanX on 2017/9/24.
 */

@JsonObject
public class Message {
    /*
 * Annotate a field that you want sent with the @JsonField marker.
 */
    @JsonField
    public String description;

}
