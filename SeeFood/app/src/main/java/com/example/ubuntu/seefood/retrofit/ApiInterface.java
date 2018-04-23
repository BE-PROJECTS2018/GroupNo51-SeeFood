package com.example.ubuntu.seefood.retrofit;


import com.example.ubuntu.seefood.yummly_pojo.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by jayesh on 10/3/18.
 */

public interface ApiInterface {

    @GET("c7ffh")
    Call<SearchResult> getSearchResults();

    @GET
    Call<SearchResult> getYummlySearchResults(@Url String parameters);

}
