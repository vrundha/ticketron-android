package com.example.eventsearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventTabFragment newInstance(String param1, String param2) {
        EventTabFragment fragment = new EventTabFragment();
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

        View v = inflater.inflate(R.layout.fragment_event_tab, container, false);

            JSONObject tmEventDetails;
        try {
            tmEventDetails = new JSONObject(getArguments().getString("json"));

            TextView tv_artist = (TextView) v.findViewById(R.id.artist);
            JSONArray _artist = tmEventDetails.getJSONObject("_embedded").getJSONArray("attractions");
            JSONObject artist = _artist.getJSONObject(0);
            tv_artist.setText(artist.getString("name"));

            TextView tv_venue = (TextView) v.findViewById(R.id.venue);
            JSONArray _venue = tmEventDetails.getJSONObject("_embedded").getJSONArray("venues");
            JSONObject venue = _venue.getJSONObject(0);
            tv_venue.setText(venue.getString("name"));

            TextView tv_pr = (TextView) v.findViewById(R.id.pr);
            JSONArray _pr = tmEventDetails.getJSONArray("priceRanges");
            JSONObject pr = _pr.getJSONObject(0);
            tv_pr.setText(pr.getString("min") + '-' + pr.getString("max") + ' ' + pr.getString("currency"));

            TextView tv_cat = (TextView) v.findViewById(R.id.cat);
            JSONArray _cat = tmEventDetails.getJSONArray("classifications");
            JSONObject cat = _cat.getJSONObject(0);
            tv_cat.setText(cat.getJSONObject("segment").getString("name") + " | " + cat.getJSONObject("genre").getString("name") + " | " +
                                    cat.getJSONObject("subGenre").getString("name"));
            TextView tv_status = (TextView) v.findViewById(R.id.status);
            tv_status.setText(tmEventDetails.getJSONObject("dates").getJSONObject("status").getString("code"));

            TextView tv_date = (TextView) v.findViewById(R.id.date);
            tv_date.setText(tmEventDetails.getJSONObject("dates").getJSONObject("start").getString("localDate"));

            TextView tv_tm = (TextView) v.findViewById(R.id.tm);
            tv_tm.setText(Html.fromHtml("<a href=\"" + tmEventDetails.getString("url") + "\">TicketMaster</a>"));
            tv_tm.setMovementMethod(LinkMovementMethod.getInstance());

            TextView tv_sm = (TextView) v.findViewById(R.id.sm);
            tv_sm.setText(Html.fromHtml("<a href=\"" + tmEventDetails.getJSONObject("seatmap").getString("staticUrl") + "\">View Seat Map Here</a>"));
            tv_sm.setMovementMethod(LinkMovementMethod.getInstance());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }


}