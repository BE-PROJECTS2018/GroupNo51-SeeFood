package com.example.ubuntu.seefood.yummly_pojo;

/**
 * Created by jayesh on 10/3/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Criteria {

    @SerializedName("allowedIngredient")
    @Expose
    private List<String> allowedIngredient = null;
    @SerializedName("allowedDiet")
    @Expose
    private List<String> allowedDiet;
    @SerializedName("q")
    @Expose
    private Object q;
    @SerializedName("excludedIngredient")
    @Expose
    private Object excludedIngredient;

    public List<String> getAllowedIngredient() {
        return allowedIngredient;
    }

    public void setAllowedIngredient(List<String> allowedIngredient) {
        this.allowedIngredient = allowedIngredient;
    }

    public List<String> getAllowedDiet() {
        return allowedDiet;
    }

    public void setAllowedDiet(List<String> allowedDiet) {
        this.allowedDiet = allowedDiet;
    }

    public Object getQ() {
        return q;
    }

    public void setQ(Object q) {
        this.q = q;
    }

    public Object getExcludedIngredient() {
        return excludedIngredient;
    }

    public void setExcludedIngredient(Object excludedIngredient) {
        this.excludedIngredient = excludedIngredient;
    }

}
