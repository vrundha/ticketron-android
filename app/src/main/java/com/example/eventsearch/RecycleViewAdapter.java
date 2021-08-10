package com.example.eventsearch;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>  {

    JSONArray tmEventList;
    Context context;
    Favorite favorite;
    public RecycleViewAdapter(JSONArray tmEventList, Favorite favorite, Context context) {
        this.tmEventList = tmEventList;
        this.context = context;
        this.favorite = favorite;
    }

    public RecycleViewAdapter(Favorite favorite, Context context) {
        this.tmEventList = favorite.getFavorites();
        this.context = context;
        this.favorite = favorite;
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.context.getResources().getIdentifier(imageName, "drawable", this.context.getPackageName());
        return drawableResourceId;
    }


    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_search_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        try {
            JSONObject event = (JSONObject) tmEventList.get(position);
            String event_name = event.getString("name");
            String event_name_crop = "";
            if(event_name.length() > 30) {
                event_name_crop = event_name.substring(0, 27) + "...";
            }
            else {
                event_name_crop = event_name;
            }
            holder.tv_name.setText(event_name_crop);
            JSONArray _venue = event.getJSONObject("_embedded").getJSONArray("venues");
            JSONObject venue = _venue.getJSONObject(0);
            holder.tv_venue.setText(venue.getString("name"));
            holder.tv_date.setText(event.getJSONObject("dates").getJSONObject("start").getString("localDate"));
            String category ="";
            try {
                category = event.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(0).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (category) {
                    case "Sports":
                        Glide.with(this.context).load(getImage("ic_sport_icon")).into(holder.iv_img);
                        break;
                    case "Music":
                        Glide.with(this.context).load(getImage("music_icon")).into(holder.iv_img);
                        break;
                    case "Arts & Theatre":
                        Glide.with(this.context).load(getImage("art_icon")).into(holder.iv_img);
                        break;
                    case "Film":
                        Glide.with(this.context).load(getImage("film_icon")).into(holder.iv_img);
                        break;
                    default:
                        Glide.with(this.context).load(getImage("miscellaneous_icon")).into(holder.iv_img);
                        break;
                }

            JSONObject tmEvent = (JSONObject) tmEventList.get(position);
            if (favorite.isFavorite(tmEvent.getString("id")))  {
                Glide.with(this.context).load(getImage("heart_fill_red")).into(holder.iv_fav);
            }
            else {
                Glide.with(this.context).load(getImage("heart_outline_black")).into(holder.iv_fav);
            }

//            Picasso.get().load(uri).into(holder.iv_img);

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventDetailsActivity.class);
                    try {
                        intent.putExtra("id", event.getString("id"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            holder.iv_fav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        JSONObject tmEvent = (JSONObject) tmEventList.get(position);
                        if (favorite.isFavorite(tmEvent.getString("id")))  {
                            favorite.removeFavorite(tmEvent.getString("id"));
                            Glide.with(context).load(getImage("heart_outline_black")).into(holder.iv_fav);
                        }
                        else {
                            favorite.addFavorite(tmEvent);
                            Glide.with(context).load(getImage("heart_fill_red")).into(holder.iv_fav);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return tmEventList.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_img;
        TextView tv_name;
        TextView tv_venue;
        TextView tv_date;
        ImageView iv_fav;
        ConstraintLayout parentLayout;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_listImg);
            tv_name = itemView.findViewById(R.id.tv_listName);
            tv_venue = itemView.findViewById(R.id.tv_listVenue);
            tv_date = itemView.findViewById(R.id.tv_listDate);
            iv_fav = itemView.findViewById(R.id.iv_favImg);
            parentLayout = itemView.findViewById(R.id.OneLineSearchListLayout);
        }
    }
}
