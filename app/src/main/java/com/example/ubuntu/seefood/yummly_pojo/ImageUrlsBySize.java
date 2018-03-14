package com.example.ubuntu.seefood.yummly_pojo;

/**
 * Created by jayesh on 12/3/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUrlsBySize {

    @SerializedName("90")
    @Expose
    private String _90;

    public String get90() {
        return _90;
    }

    public void set90(String _90) {
        this._90 = _90;
    }

}
