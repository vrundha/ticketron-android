package com.example.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventsearch.ui.main.SectionsPagerAdapter2;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.eventsearch.ui.main.SectionsPagerAdapter;
import com.example.eventsearch.databinding.ActivityEventDetailsBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailsActivity extends AppCompatActivity {

    String id;
    private ActivityEventDetailsBinding binding;
    private RequestQueue queue;
    private JSONObject tmEventDetails = null;
    Bundle bundle;
    SharedPreferences sp;


    void sendBundle(Bundle bundle) {
        SectionsPagerAdapter2 sectionsPagerAdapter = new SectionsPagerAdapter2(EventDetailsActivity.this, getSupportFragmentManager(), bundle);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.detailsTabs;
        tabs.setupWithViewPager(viewPager);
        setupTabIcons(tabs);

        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        bundle = new Bundle();
        ActionBar actionBar = getSupportActionBar();
        this.sp = getSharedPreferences("fav", Context.MODE_PRIVATE);

        binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        id = getIntent().getStringExtra("id");
        String url = "https://csci571-hw8-amb.wl.r.appspot.com/api/details?eventid=" + id;

        JsonObjectRequest jsonObjectRequestEvent = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                tmEventDetails = response;
                bundle.putString("json", tmEventDetails.toString());
                try {
                    String eventHeaderName = tmEventDetails.getString("name");
                    actionBar.setTitle(eventHeaderName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



//----------------------------Spotify-----------------------------------------------
                try {
                    JSONArray _name = tmEventDetails.getJSONObject("_embedded").getJSONArray("attractions");
                    JSONObject name = _name.getJSONObject(0);
                    String att_name = name.getString("name");
                    String url = "https://csci571-hw8-amb.wl.r.appspot.com/api/spotify?attraction=" + att_name.replace(" ","+");

                    JsonObjectRequest jsonObjectRequestSpotify = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject tmSpotify = null;
                            tmSpotify = response;
                            bundle.putString("json2", tmSpotify.toString());

 //---------------------------------------Venue--------------------------------------------------
                            try {

                                JSONArray _venue = tmEventDetails.getJSONObject("_embedded").getJSONArray("venues");
                                JSONObject venue = _venue.getJSONObject(0);
                                String id = venue.getString("id");
                                String url = "https://csci571-hw8-amb.wl.r.appspot.com/api/venue?venueid=" + id;

                                JsonObjectRequest jsonObjectRequestVenue = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        JSONObject tmVenue = null;
                                        tmVenue = response;
                                        bundle.putString("json3", tmVenue.toString());
                                        sendBundle(bundle);

                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                        Log.e("Volley", error.toString());
                                    }
                                });
                                queue.add(jsonObjectRequestVenue);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//-----------------------------------------------------------------------------------------------

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Log.e("Volley", error.toString());
                        }
                    });
                    queue.add(jsonObjectRequestSpotify);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//------------------------------------------------------------------------------------

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.e("Volley", error.toString());
            }
        });

        queue.add(jsonObjectRequestEvent);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.buttons, menu);

        Favorite fav = new Favorite(this.sp);
        if(fav.isFavorite(id))
        {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.heart_fill_red));
        }
        else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.heart_outline_black));
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_twitter:
                Intent browserIntent = null;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check+out+"+tmEventDetails.getString("name")+"+at+"
                                                                                +tmEventDetails.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(browserIntent);
                return true;
            case  R.id.action_fav:
                Favorite fav = new Favorite(sp);
                if(fav.isFavorite(id))
                {
                    fav.removeFavorite(id);
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.heart_outline_black));
                }
                else {
                    fav.addFavorite(tmEventDetails);
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.heart_fill_red));
                }
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
//            case R.id.help:
//                showHelp();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupTabIcons(TabLayout tabLayout) {
        int[] tabIcons = {
                R.drawable.info_outline,
                R.drawable.artist,
                R.drawable.venue

        };
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

    }



}

