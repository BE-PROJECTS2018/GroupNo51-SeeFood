package com.example.ubuntu.seefood.recipes;

import com.example.ubuntu.seefood.yummly_pojo.Attributes;
import com.example.ubuntu.seefood.yummly_pojo.Flavors;

import java.util.List;

/**
 * Created by jayesh on 23/4/18.
 */

public class StarredRecipe {

    public String recipename;
    public Long totaltime;
    public List<String> ingredients;
    public Float rating;
    public Attributes tags;
    public String link;
    public Flavors flavors;
}
