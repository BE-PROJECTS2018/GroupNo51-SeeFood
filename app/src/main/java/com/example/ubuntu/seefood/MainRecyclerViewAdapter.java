package com.example.ubuntu.seefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ubuntu.seefood.recipes.StarredRecipe;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

/**
 * Created by ubuntu on 15/3/18.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    ArrayList<StarredRecipe> list;
    Context context;
    SharedPreferences preferences;

    public MainRecyclerViewAdapter(ArrayList<StarredRecipe> list, Context context) {
        this.list = list;
        this.context = context;
        preferences = context.getSharedPreferences("Starred", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.main_recyclerview_single_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/AlanisHand.ttf");
        holder.recipeName.setTypeface(typeface);
        holder.recipeName.setText(list.get(position).recipename);
        holder.rating.setText("Rating:" + list.get(position).rating);
        holder.star.setLiked(true);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnLikeListener, View.OnClickListener {
        TextView recipeName;
        TextView rating;
        LikeButton star;
        CardView item;

        public MyViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            rating = itemView.findViewById(R.id.mainRating);
            star = itemView.findViewById(R.id.mainStarButton);
            item = (CardView)itemView.findViewById(R.id.main_single_item);
            item.setOnClickListener(this);
            star.setOnLikeListener(this);
        }

        @Override
        public void liked(LikeButton likeButton) {
            notifyDataSetChanged();
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            likeButton.setLiked(false);
            SharedPreferences.Editor prefEditor = preferences.edit();
            prefEditor.remove(list.get(getAdapterPosition()).recipename);
            prefEditor.commit();
            list.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), list.size());
        }

        @Override
        public void onClick(View v) {
            StarredRecipe recipe = list.get(getAdapterPosition());
            Gson gson = new Gson();
            String json = gson.toJson(recipe);
            Intent intent = new Intent(context, MainRecyclerItemClickActivity.class);
            intent.putExtra("Item", json);
            context.startActivity(intent);
        }
    }
}
