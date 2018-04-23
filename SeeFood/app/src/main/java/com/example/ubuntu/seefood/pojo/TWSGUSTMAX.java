package com.example.ubuntu.seefood.pojo;

/**
 * Created by jayesh on 10/3/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TWSGUSTMAX {

    @SerializedName("TIME_STRING")
    @Expose
    private String tIMESTRING;
    @SerializedName("VALUE")
    @Expose
    private String vALUE;

    public String getTIMESTRING() {
        return tIMESTRING;
    }
}
