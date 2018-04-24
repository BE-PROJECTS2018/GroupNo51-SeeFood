package com.example.ubuntu.seefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.seefood.recipes.StarredRecipe;
import com.example.ubuntu.seefood.yummly_pojo.Attributes;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

public class MainRecyclerItemClickActivity extends AppCompatActivity implements OnLikeListener {

    TextView recipeName;
    TextView ingridientTitle;
    TextView ingridientsList;
    TextView tasteTtile;
    SeekBar saltySeekBar;
    SeekBar sourSeekBar;
    SeekBar sweetSeekBar;
    SeekBar bitterSeekBar;
    SeekBar meatySeekBar;
    SeekBar piquantSeekBar;
    TextView ratingTitle;
    RatingBar ratingBar;
    TextView prepTime;
    TextView link;
    TextView tagsTitle;
    TextView tagsListTextView;
    TextView prepTitle;
    LinearLayout tasteSeekBars;
    LikeButton star;
    StarredRecipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_item);

        FrameLayout frameLayout = this.findViewById(R.id.mainFrameLayout);
        frameLayout.setBackgroundResource(R.drawable.wood_background);

        Intent intent = getIntent();
        String object = intent.getStringExtra("Item");
        Gson gson = new Gson();
        recipe = gson.fromJson(object, StarredRecipe.class);

        recipeName = findViewById(R.id.recipeName);
        ingridientTitle = findViewById(R.id.ingridients_title);
        ingridientsList = findViewById(R.id.ingridentsList);
        tasteTtile = findViewById(R.id.taste_title);
        saltySeekBar = findViewById(R.id.saltySeekBar);
        sourSeekBar = findViewById(R.id.sourSeekBar);
        sweetSeekBar = findViewById(R.id.sweetSeekBar);
        bitterSeekBar = findViewById(R.id.bitterSeekBar);
        meatySeekBar = findViewById(R.id.meatySeekBar);
        piquantSeekBar = findViewById(R.id.piquantSeekBar);
        ratingTitle = findViewById(R.id.rating_title);
        ratingBar = findViewById(R.id.rating_bar);
        prepTime = findViewById(R.id.prep_time);
        link = findViewById(R.id.link);
        tagsTitle = findViewById(R.id.tags_title);
        tagsListTextView = findViewById(R.id.tagsList);
        prepTitle = findViewById(R.id.prep_title);
        tasteSeekBars = findViewById(R.id.taste_seekbars);
        star = findViewById(R.id.starButton);
        star.setOnLikeListener(this);


        Typeface ingridientList = Typeface.createFromAsset(getAssets(), "fonts/AlanisHand.ttf");
        Typeface recipeTitleFont = Typeface.createFromAsset(getAssets(), "fonts/Quikhand.ttf");
        recipeName.setTypeface(recipeTitleFont);
        recipeName.setText(recipe.recipename);

        Typeface ingridientsTitleFont = Typeface.createFromAsset(getAssets(), "fonts/pigment.otf");
        ingridientTitle.setTypeface(ingridientsTitleFont);


        if (recipe.ingredients == null) {
            ingridientTitle.setVisibility(View.GONE);
            ingridientsList.setVisibility(View.GONE);
        } else {

            List<String> list = recipe.ingredients;
            StringBuilder builder = new StringBuilder(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                builder.append(", ").append(list.get(i));
            }

            ingridientsList.setTypeface(ingridientList);
            ingridientsList.setText(builder.toString());
        }

        tasteTtile.setTypeface(ingridientsTitleFont);

        if (recipe.flavors == null) {
            tasteTtile.setVisibility(View.GONE);
            tasteSeekBars.setVisibility(View.GONE);
        } else {

            saltySeekBar.setProgress((int) (recipe.flavors.getSalty() * 100));
            sourSeekBar.setProgress((int) (recipe.flavors.getSour() * 100));
            sweetSeekBar.setProgress((int) (recipe.flavors.getSweet() * 100));
            bitterSeekBar.setProgress((int) (recipe.flavors.getBitter() * 100));
            meatySeekBar.setProgress((int) (recipe.flavors.getMeaty() * 100));
            piquantSeekBar.setProgress((int) (recipe.flavors.getPiquant() * 100));
            saltySeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            sourSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            sweetSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            bitterSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            meatySeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            piquantSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }

        if (recipe.rating == null) {
            ratingBar.setVisibility(View.GONE);
            ratingTitle.setVisibility(View.GONE);
        } else {

            ratingTitle.setTypeface(ingridientsTitleFont);

            ratingBar.setNumStars(5);
            ratingBar.setMax(5);
            ratingBar.setStepSize(0.1f);
            ratingBar.setIsIndicator(true);
            ratingBar.setRating(recipe.rating);

        }

        if (recipe.totaltime == null) {
            prepTitle.setVisibility(View.GONE);
            prepTime.setVisibility(View.GONE);
        } else {

            long totalTime = recipe.totaltime;
            prepTime.setText("" + (totalTime / 60) + " minutes");

            Typeface minutesFont = Typeface.createFromAsset(getAssets(), "fonts/SqueakyChalkSound.ttf");

            prepTitle.setTypeface(ingridientsTitleFont);
            prepTime.setTypeface(ingridientList);
        }

        link.setTypeface(ingridientList);
        link.setClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setText(Html.fromHtml(recipe.link));

        if (recipe.tags == null) {
            tagsTitle.setVisibility(View.GONE);
            tagsListTextView.setVisibility(View.GONE);
        } else {
            tagsTitle.setTypeface(ingridientsTitleFont);
            Attributes attributes = recipe.tags;
            List<String> courses = attributes.getCourse();
            List<String> cuisines = attributes.getCuisine();
            List<String> holidays = attributes.getHoliday();


            StringBuilder tagsList = new StringBuilder();

            if (courses != null) {
                for (int i = 0; i < courses.size(); i++) {
                    tagsList.append(courses.get(i)).append(", ");
                }
            }

            if (cuisines != null) {
                for (int i = 0; i < cuisines.size(); i++) {
                    tagsList.append(cuisines.get(i)).append(", ");
                }
            }

            if (holidays != null) {
                for (int i = 0; i < holidays.size(); i++) {
                    tagsList.append(holidays.get(i)).append(", ");
                }
            }


            tagsListTextView.setTypeface(ingridientList);
            tagsListTextView.setText(tagsList.toString());
        }
        star.setLiked(true);

    }

    @Override
    public void liked(LikeButton likeButton) {
        likeButton.setLiked(true);

        SharedPreferences preferences = getSharedPreferences("Starred", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        prefEditor.putString(recipe.recipename, json);
        prefEditor.commit();

        Toast.makeText(this, "Starred:" + recipe.recipename, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void unLiked(LikeButton likeButton) {
        likeButton.setLiked(false);
        SharedPreferences preferences = getSharedPreferences("Starred", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.remove(recipe.recipename);
        prefEditor.commit();
        Toast.makeText(this, "Deleted:" + recipe.recipename, Toast.LENGTH_SHORT).show();
    }
}
