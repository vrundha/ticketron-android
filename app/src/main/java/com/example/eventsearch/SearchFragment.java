package com.example.eventsearch;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
//        return inflater.inflate(R.layout.fragment_search, container, false);

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        EditText keyword_editText = (EditText) v.findViewById(R.id.keyword);
        EditText distance_editText = (EditText) v.findViewById(R.id.distance);
        Button reset_btn=(Button) v.findViewById(R.id.reset_button);

        String[] cat_values =
                {"All", "Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous"};
        Spinner cat_spinner = (Spinner) v.findViewById(R.id.category);
        ArrayAdapter<String> cat_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cat_values);
        cat_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        cat_spinner.setAdapter(cat_adapter);

        String[] unit_values = {"Miles", "Kilometers"};
        Spinner unit_spinner = (Spinner) v.findViewById(R.id.unit);
        ArrayAdapter<String> unit_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, unit_values);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        unit_spinner.setAdapter(unit_adapter);

        EditText loc_editText=(EditText) v.findViewById(R.id.location);
        loc_editText.setEnabled(false);
        loc_editText.setInputType(InputType.TYPE_NULL);
        loc_editText.setFocusableInTouchMode(false);

        RadioButton from_radioButton = (RadioButton) v.findViewById(R.id.current_location);
        reset_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                keyword_editText.getText().clear();
                loc_editText.getText().clear();
                unit_spinner.setSelection(0);
                cat_spinner.setSelection(0);
                distance_editText.getText().clear();
                from_radioButton.setChecked(true);
                keyword_editText.setError(null);
                loc_editText.setError(null);
            }
        });

        return v;


    }


}