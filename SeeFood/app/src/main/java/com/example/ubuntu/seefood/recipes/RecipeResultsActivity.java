package com.example.ubuntu.seefood.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.Toast;

import com.example.ubuntu.seefood.R;
import com.example.ubuntu.seefood.pojo.Result;
import com.example.ubuntu.seefood.retrofit.ApiClient;
import com.example.ubuntu.seefood.retrofit.ApiInterface;
import com.example.ubuntu.seefood.yummly_pojo.Match;
import com.example.ubuntu.seefood.yummly_pojo.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyRecyclerAdapter adapter;
    private List<Result> result;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_results);

        recyclerView = findViewById(R.id.results_recycler_view);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String parameter = bundle.getString("param");

        //Toast.makeText(getApplicationContext(), "" + parameter, Toast.LENGTH_SHORT).show();

        Call<SearchResult> call = apiInterface.getSearchResults();

        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                Toast.makeText(getApplicationContext(), "Api call success", Toast.LENGTH_LONG).show();
                SearchResult searchResult = response.body();
                List<Match> matchList = searchResult.getMatches();
                adapter = new MyRecyclerAdapter(matchList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Api call Failed:" + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}
