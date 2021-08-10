package com.example.eventsearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MapView mMapView;
    private GoogleMap googleMap;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VenueTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueTabFragment newInstance(String param1, String param2) {
        VenueTabFragment fragment = new VenueTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_venue_tab, container, false);
        JSONObject venueDetails;
        try {
            venueDetails = new JSONObject(getArguments().getString("json3"));

            try {
                TextView tv_name = (TextView) v.findViewById(R.id.venue_name);
                tv_name.setText(venueDetails.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                TextView tv_addr = (TextView) v.findViewById(R.id.venue_addr);
                tv_addr.setText(venueDetails.getJSONObject("address").getString("line1"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                TextView tv_city = (TextView) v.findViewById(R.id.venue_city);
                tv_city.setText(venueDetails.getJSONObject("city").getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                TextView tv_ph = (TextView) v.findViewById(R.id.venue_ph);
                tv_ph.setText(venueDetails.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail"));

            } catch (JSONException e) {
                TextView tv_ph_label = (TextView) v.findViewById(R.id.venue_ph_label);
                tv_ph_label.setText("");
                e.printStackTrace();
            }
            try {
                TextView tv_hrs = (TextView) v.findViewById(R.id.venue_hrs);
                tv_hrs.setText(venueDetails.getJSONObject("boxOfficeInfo").getString("openHoursDetail"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                TextView tv_gen = (TextView) v.findViewById(R.id.venue_gen);
                tv_gen.setText(venueDetails.getJSONObject("generalInfo").getString("generalRule"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                TextView tv_child = (TextView) v.findViewById(R.id.venue_child);
                tv_child.setText(venueDetails.getJSONObject("generalInfo").getString("childRule"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String lat = venueDetails.getJSONObject("location").getString("latitude");
            String lng = venueDetails.getJSONObject("location").getString("longitude");


            mMapView = (MapView) v.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume(); // needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    // For showing a move to my location button
//                    googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }





        return v;
    }
}