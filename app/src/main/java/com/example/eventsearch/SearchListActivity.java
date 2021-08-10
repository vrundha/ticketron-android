package com.example.eventsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        getSupportActionBar().setTitle("Search Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().hasExtra("json")) {
            JSONArray tmEventList = null;
            try {
                tmEventList = new JSONArray(getIntent().getStringExtra("json"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Favorite favorite = new Favorite(getSharedPreferences("fav", Context.MODE_PRIVATE));


            recyclerView = findViewById(R.id.lv_searchList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new RecycleViewAdapter(tmEventList, favorite, this);
            recyclerView.setAdapter(mAdapter);
        }
    }


}