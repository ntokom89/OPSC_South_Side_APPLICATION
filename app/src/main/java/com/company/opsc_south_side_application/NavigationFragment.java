package com.company.opsc_south_side_application;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends Fragment {

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
        return view;
    }
}