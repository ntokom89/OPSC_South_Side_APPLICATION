package com.company.opsc_south_side_application;

import static com.company.opsc_south_side_application.MainActivity.dest;
import static com.company.opsc_south_side_application.MainActivity.fragmentType;
import static com.company.opsc_south_side_application.MainActivity.getDirectionsUrl;
import static com.company.opsc_south_side_application.MainActivity.origin;
import static com.company.opsc_south_side_application.MainActivity.title;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends DialogFragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton motor;
    ImageButton walkButton;
    ImageButton trainButton;
    ImageButton bicycleButton;
    ImageButton exitButton;
    ImageButton favouriteButton;
    ImageButton settingsButton;
    Button buttonStartNav;
    TextView distance, duration, userLocation;
    TextView destLocation;
    String mode = "";
    Boolean  chosenMode = false;



    public NavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NavigationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavigationFragment newInstance(String param1, String param2) {
        NavigationFragment fragment = new NavigationFragment();
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
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        walkButton = view.findViewById(R.id.imageButtonWalkMode);
        motor = view.findViewById(R.id.imageButtonCarMode);
        bicycleButton = view.findViewById(R.id.imageButtonCycleMode);
        trainButton = view.findViewById(R.id.imageButtonTrainMode);
        favouriteButton = view.findViewById(R.id.imageButtonFavourite);
        settingsButton = view.findViewById(R.id.imageButtonSetting);
        buttonStartNav = view.findViewById(R.id.buttonStartNav);
        duration = view.findViewById(R.id.textViewHours);
        distance = view.findViewById(R.id.textViewDistance);
        exitButton = view.findViewById(R.id.imageButtonClose);
        userLocation = view.findViewById(R.id.textViewMyLocation);
        destLocation = view.findViewById(R.id.textViewDestLocation);


        //Boolean chosenMode = false;
        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosenMode){
                    mode = "";
                    chosenMode = false;
                }else{
                    mode = "mode=walking";
                    walkButton.setBackgroundColor(000000);
                    chosenMode = true;
                }
            }
        });
        motor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosenMode){
                    mode = "";
                    chosenMode = false;
                }else{
                    mode = "mode=driving";
                    walkButton.setBackgroundColor(000000);
                    chosenMode = true;
                }
            }
        });
        bicycleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosenMode){
                    mode = "";
                    chosenMode = false;
                }else{
                    mode = "mode=BICYCLING";
                    walkButton.setBackgroundColor(000000);
                    chosenMode = true;
                }
            }
        });
        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosenMode){
                    mode = "";
                    chosenMode = false;
                }else{
                    mode = "mode=transit";
                    walkButton.setBackgroundColor(000000);
                    chosenMode = true;
                }
            }
        });
        buttonStartNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL urlConnection;
                String url;
                try {
                    url = getDirectionsUrl(origin, dest, mode);
                    fragmentType = "Main Nav";
                    urlConnection = new URL(url);
                    MainActivity main = new MainActivity();
                    main.impelemntFetchDirection(urlConnection);
                    //new MainActivity.fetchDirectionsData().execute(urlConnection);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = getDialog();
                d.dismiss();
            }
        });
        return view;
    }

    public void setUpFragmentUi(String distanceS, String durationS){
        duration.setText(durationS);
        distance.setText(distanceS);
    }

    public void setUpFragmentUiAddress(String destLocationS){
        //Log.d("TitleUpload", destLocationS);
        destLocation.setText(destLocationS);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
        setUpFragmentUiAddress(title);
    }
}