package com.example.eventsearch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArtistTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistTabFragment newInstance(String param1, String param2) {
        ArtistTabFragment fragment = new ArtistTabFragment();
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


        View v = inflater.inflate(R.layout.fragment_artist_tab, container, false);

        JSONObject spotifyDetails;
        try {
            spotifyDetails = new JSONObject(getArguments().getString("json2"));
            spotifyDetails = spotifyDetails.getJSONObject("artists").getJSONArray("items").getJSONObject(0);


            try {
                TextView tv_name = (TextView) v.findViewById(R.id.spot_name);
                tv_name.setText(spotifyDetails.getString("name"));
                TextView tv_set_label = (TextView) v.findViewById(R.id.spot_name_label);
                tv_set_label.setText("Name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                TextView tv_fol = (TextView) v.findViewById(R.id.spot_fol);
                tv_fol.setText(spotifyDetails.getJSONObject("followers").getString("total"));
                TextView tv_set_label = (TextView) v.findViewById(R.id.spot_fol_label);
                tv_set_label.setText("Followers");
            } catch (JSONException e) {

                e.printStackTrace();
            }

            try {
                TextView tv_pop = (TextView) v.findViewById(R.id.spot_pop);
                tv_pop.setText(spotifyDetails.getString("popularity"));
                TextView tv_set_label = (TextView) v.findViewById(R.id.spot_pop_label);
                tv_set_label.setText("Popularity");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                TextView tv_url = (TextView) v.findViewById(R.id.spot_url);
                tv_url.setText(Html.fromHtml("<a href=\"" + spotifyDetails.getJSONObject("external_urls").getString("spotify") + "\">Spotify</a>"));
                tv_url.setMovementMethod(LinkMovementMethod.getInstance());
                TextView tv_set_label = (TextView) v.findViewById(R.id.spot_url_label);
                tv_set_label.setText("Check At");
            } catch (JSONException e) {

                e.printStackTrace();
            }


        } catch (
                JSONException e) {
                TextView tv_name = (TextView) v.findViewById(R.id.spot_name);
                tv_name.setText("No Details");
            tv_name.setTextSize(22);


            e.printStackTrace();
        }


        return v;
    }
}