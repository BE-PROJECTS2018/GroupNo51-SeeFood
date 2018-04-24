package com.example.ubuntu.seefood.recipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubuntu.seefood.R;
import com.example.ubuntu.seefood.yummly_pojo.Attributes;
import com.example.ubuntu.seefood.yummly_pojo.Match;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

/**
 * Created by jayesh on 10/3/18.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    Context context;
    private List<Match> matchList;
    SharedPreferences preferences;

    public MyRecyclerAdapter(List<Match> matchList, Context context) {
        this.matchList = matchList;
        this.context = context;
        preferences = context.getSharedPreferences("Starred", Context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Typeface ingridientList = Typeface.createFromAsset(context.getAssets(), "fonts/AlanisHand.ttf");
        Typeface recipeTitleFont = Typeface.createFromAsset(context.getAssets(), "fonts/Quikhand.ttf");
        holder.recipeName.setTypeface(recipeTitleFont);
        holder.recipeName.setText(matchList.get(position).getRecipeName());

        Typeface ingridientsTitleFont = Typeface.createFromAsset(context.getAssets(), "fonts/pigment.otf");
        holder.ingridientTitle.setTypeface(ingridientsTitleFont);


        if (matchList.get(position).getIngredients() == null) {
            holder.ingridientTitle.setVisibility(View.GONE);
            holder.ingridientsList.setVisibility(View.GONE);
        } else {

            List<String> list = matchList.get(position).getIngredients();
            StringBuilder builder = new StringBuilder(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                builder.append(", ").append(list.get(i));
            }

            holder.ingridientsList.setTypeface(ingridientList);
            holder.ingridientsList.setText(builder.toString());
        }

        holder.tasteTtile.setTypeface(ingridientsTitleFont);

        if (matchList.get(position).getFlavors() == null) {
            holder.tasteTtile.setVisibility(View.GONE);
            holder.tasteSeekBars.setVisibility(View.GONE);
        } else {

            holder.saltySeekBar.setProgress((int) (matchList.get(position).getFlavors().getSalty() * 100));
            holder.sourSeekBar.setProgress((int) (matchList.get(position).getFlavors().getSour() * 100));
            holder.sweetSeekBar.setProgress((int) (matchList.get(position).getFlavors().getSweet() * 100));
            holder.bitterSeekBar.setProgress((int) (matchList.get(position).getFlavors().getBitter() * 100));
            holder.meatySeekBar.setProgress((int) (matchList.get(position).getFlavors().getMeaty() * 100));
            holder.piquantSeekBar.setProgress((int) (matchList.get(position).getFlavors().getPiquant() * 100));
            holder.saltySeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            holder.sourSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            holder.sweetSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            holder.bitterSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            holder.meatySeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            holder.piquantSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }

        if (matchList.get(position).getRating() == null) {
            holder.ratingBar.setVisibility(View.GONE);
            holder.ratingTitle.setVisibility(View.GONE);
        } else {

            holder.ratingTitle.setTypeface(ingridientsTitleFont);

            holder.ratingBar.setNumStars(5);
            holder.ratingBar.setMax(5);
            holder.ratingBar.setStepSize(0.1f);
            holder.ratingBar.setIsIndicator(true);
            holder.ratingBar.setRating(matchList.get(position).getRating());

        }

        if (matchList.get(position).getTotalTimeInSeconds() == null) {
            holder.prepTitle.setVisibility(View.GONE);
            holder.prepTime.setVisibility(View.GONE);
        } else {

            long totalTime = matchList.get(position).getTotalTimeInSeconds();
            holder.prepTime.setText("" + (totalTime / 60) + " minutes");

            Typeface minutesFont = Typeface.createFromAsset(context.getAssets(), "fonts/SqueakyChalkSound.ttf");

            holder.prepTitle.setTypeface(ingridientsTitleFont);
            holder.prepTime.setTypeface(ingridientList);
        }

        holder.link.setTypeface(ingridientList);
        holder.link.setClickable(true);
        holder.link.setMovementMethod(LinkMovementMethod.getInstance());
        String link = "https://www.yummly.com/#recipe/" + matchList.get(position).getId();
        holder.link.setText(Html.fromHtml(link));

        if (matchList.get(position).getAttributes() == null) {
            holder.tagsTitle.setVisibility(View.GONE);
            holder.tagsList.setVisibility(View.GONE);
        } else {
            holder.tagsTitle.setTypeface(ingridientsTitleFont);
            Attributes attributes = matchList.get(position).getAttributes();
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

            holder.tagsList.setTypeface(ingridientList);
            holder.tagsList.setText(tagsList.toString());
        }


        if(preferences.contains(matchList.get(position).getRecipeName())){
            holder.star.setLiked(true);
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnLikeListener {

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
        TextView tagsList;
        TextView prepTitle;
        LinearLayout tasteSeekBars;
        LikeButton star;

        public MyViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            ingridientTitle = itemView.findViewById(R.id.ingridients_title);
            ingridientsList = itemView.findViewById(R.id.ingridentsList);
            tasteTtile = itemView.findViewById(R.id.taste_title);
            saltySeekBar = itemView.findViewById(R.id.saltySeekBar);
            sourSeekBar = itemView.findViewById(R.id.sourSeekBar);
            sweetSeekBar = itemView.findViewById(R.id.sweetSeekBar);
            bitterSeekBar = itemView.findViewById(R.id.bitterSeekBar);
            meatySeekBar = itemView.findViewById(R.id.meatySeekBar);
            piquantSeekBar = itemView.findViewById(R.id.piquantSeekBar);
            ratingTitle = itemView.findViewById(R.id.rating_title);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            prepTime = itemView.findViewById(R.id.prep_time);
            link = itemView.findViewById(R.id.link);
            tagsTitle = itemView.findViewById(R.id.tags_title);
            tagsList = itemView.findViewById(R.id.tagsList);
            prepTitle = itemView.findViewById(R.id.prep_title);
            tasteSeekBars = itemView.findViewById(R.id.taste_seekbars);
            star = itemView.findViewById(R.id.starButton);
            star.setOnLikeListener(this);
        }

        @Override
        public void liked(LikeButton likeButton) {
            likeButton.setLiked(true);
            StarredRecipe recipe = new StarredRecipe();
            recipe.recipename = matchList.get(getAdapterPosition()).getRecipeName();
            recipe.totaltime = matchList.get(getAdapterPosition()).getTotalTimeInSeconds();
            recipe.ingredients = matchList.get(getAdapterPosition()).getIngredients();
            recipe.rating = matchList.get(getAdapterPosition()).getRating();
            recipe.tags = matchList.get(getAdapterPosition()).getAttributes();
            recipe.flavors = matchList.get(getAdapterPosition()).getFlavors();
            recipe.link = "https://www.yummly.com/#recipe/" + matchList.get(getAdapterPosition()).getId();

            SharedPreferences.Editor prefEditor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(recipe);
            prefEditor.putString(recipe.recipename, json);
            prefEditor.commit();

            Toast.makeText(context, "Starred:" + matchList.get(getAdapterPosition()).getRecipeName(), Toast.LENGTH_SHORT).show();


        }

        @Override
        public void unLiked(LikeButton likeButton) {
            likeButton.setLiked(false);
            SharedPreferences.Editor prefEditor = preferences.edit();
            prefEditor.remove(matchList.get(getAdapterPosition()).getRecipeName());
            prefEditor.commit();
            Toast.makeText(context, "Deleted:" + matchList.get(getAdapterPosition()).getRecipeName(), Toast.LENGTH_SHORT).show();
        }
    }
}
