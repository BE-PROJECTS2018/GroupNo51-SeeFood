package com.example.ubuntu.seefood.yummly_pojo;

/**
 * Created by jayesh on 10/3/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributeRanges {

    @SerializedName("flavor-piquant")
    @Expose
    private FlavorPiquant flavorPiquant;

    public FlavorPiquant getFlavorPiquant() {
        return flavorPiquant;
    }

    public void setFlavorPiquant(FlavorPiquant flavorPiquant) {
        this.flavorPiquant = flavorPiquant;
    }

}
