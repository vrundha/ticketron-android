package com.example.eventsearch;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.eventsearch.ui.main.SectionsPagerAdapter;
import com.example.eventsearch.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sp = getSharedPreferences("fav", Context.MODE_PRIVATE);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), sp);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });

    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();

        EditText loc_editText = (EditText) findViewById(R.id.location);

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.current_location:
                loc_editText.setEnabled(false);
                loc_editText.setInputType(InputType.TYPE_NULL);
                loc_editText.setFocusableInTouchMode(false);
                loc_editText.setText("");
                break;

            case R.id.custom_location:
                loc_editText.setEnabled(true);
                loc_editText.setInputType(InputType.TYPE_CLASS_TEXT);
                loc_editText.setFocusableInTouchMode(true);
                break;
        }
    }

    public boolean validate_empty_fields(EditText key_editText, EditText loc_editText) {
        boolean valid = true;

//        EditText key_editText=(EditText) findViewById(R.id.keyword);
        String keyword = key_editText.getText().toString();
        if (keyword.matches("")) {
            key_editText.setError("Please enter mandatory field");
            valid = false;
        }

        RadioButton custom_location_radio = (RadioButton) findViewById(R.id.custom_location);
        boolean checked = ((RadioButton) custom_location_radio).isChecked();
        if (checked) {
//            EditText loc_editText = (EditText) findViewById(R.id.location);
            String location = key_editText.getText().toString();
            if (location.matches("")) {
                loc_editText.setError("Please enter mandatory field");
                valid = false;
            }
        }

        return valid;
    }


    public void performSearch(View view) {

        EditText key_editText = (EditText) findViewById(R.id.keyword);
        String keyword = key_editText.getText().toString();
        Spinner cat_spinner = (Spinner) findViewById(R.id.category);
        String category = cat_spinner.getSelectedItem().toString();
        EditText loc_editText = (EditText) findViewById(R.id.location);
        String location = loc_editText.getText().toString();
        EditText dist_editText = (EditText) findViewById(R.id.distance);
        String distance = dist_editText.getText().toString();
        Spinner unit_editText = (Spinner) findViewById(R.id.unit);
        String unit = unit_editText.getSelectedItem().toString();
        RadioButton current_loc_radioButton = (RadioButton) findViewById(R.id.current_location);


        if (validate_empty_fields(key_editText, loc_editText)) {
            double longitude = 0.0;
            double latitude = 0.0;
            if (current_loc_radioButton.isChecked()) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longitude = loc.getLongitude();
                latitude = loc.getLatitude();
            }
            String url = "https://csci571-hw8-amb.wl.r.appspot.com/api?keyword=" + keyword.replace(' ', '+') +
                    "&category=" + category + "&distance=" + distance + "&custom_location=" + location +
                    "&lat=" + ((latitude==0.0)?"":latitude) + "&lng=" + ((longitude==0.0)?"":longitude) + "&unit=" + unit;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray tmEventList = null;
                            try {
                                tmEventList = response.getJSONObject("_embedded").getJSONArray("events");
                                eventList(tmEventList);
                            } catch (JSONException e) {
                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("No records found");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                e.printStackTrace();

                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Log.e("Volley", error.toString());

                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);


        }

    }

    void eventList(JSONArray tmEventList) throws JSONException {

        Intent intent = new Intent(this, SearchListActivity.class);
        intent.putExtra("json", tmEventList.toString());
        startActivity(intent);

    }


}

