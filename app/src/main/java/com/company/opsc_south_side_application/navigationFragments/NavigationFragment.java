package com.company.opsc_south_side_application.navigationFragments;

import static com.company.opsc_south_side_application.MainActivity.buttonWhere;
//import static com.company.opsc_south_side_application.MainActivity.context;
import static com.company.opsc_south_side_application.MainActivity.databaseReference;
import static com.company.opsc_south_side_application.MainActivity.dest;
//import static com.company.opsc_south_side_application.MainActivity.dialogFragment;
import static com.company.opsc_south_side_application.MainActivity.fragmentType;
import static com.company.opsc_south_side_application.MainActivity.getDirectionsUrl;
import static com.company.opsc_south_side_application.MainActivity.metric;
import static com.company.opsc_south_side_application.MainActivity.origin;
import static com.company.opsc_south_side_application.MainActivity.place;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.company.opsc_south_side_application.MainActivity;
import com.company.opsc_south_side_application.R;
import com.company.opsc_south_side_application.models.PlacesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static Boolean isFavouritePlace;
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

        if(isFavouritePlace){
            setUpFragmentUiFavourite();
        }

        //Boolean chosenMode = false;
        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosenMode){
                    mode = "";
                    chosenMode = false;
                }else{
                    mode = "mode=walking";
                    Toast.makeText(requireContext().getApplicationContext(),"Walking mode chosen",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(requireContext().getApplicationContext(),"Driving mode chosen",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(requireContext().getApplicationContext(),"Bicycle mode chosen",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(requireContext().getApplicationContext(),"Public Transit mode chosen",Toast.LENGTH_SHORT).show();
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
                    url = getDirectionsUrl(origin, dest, mode,metric);
                    fragmentType = "Main Nav";
                    urlConnection = new URL(url);
                    MainActivity main = new MainActivity();
                    main.impelementFetchDirection(urlConnection);
                    //new MainActivity.fetchDirectionsData().execute(urlConnection);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        //backbutton to remove the fragment
        //https://developer.android.com/guide/fragments/transactions
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialog d = getDialog();
               // d.dismiss();
                getParentFragmentManager().beginTransaction().remove(NavigationFragment.this).commit();
                buttonWhere.setVisibility(View.VISIBLE);
            }
        });

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("favourite place", String.valueOf(isFavouritePlace));
                if(isFavouritePlace){
                    deletePlaceAtFirebase(place);
                    Log.d("favourite place", "deleting place at database");
                }else{
                    addPlaceToFirebase(place);
                    Log.d("favourite place", "adding place at database");
                }

            }
        });
        return view;
    }
    //Method to delete favourite place at firebase database
    //https://stackoverflow.com/questions/37390864/how-to-delete-from-firebase-realtime-database  Frank van Puffelen
    private void deletePlaceAtFirebase(PlacesModel placesModel) {
        Query query = databaseReference.child("FavouritePlaces").orderByChild("placeID").equalTo(placesModel.getPlaceID());
        Log.d("favourite place",""+placesModel.getPlaceID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    favouriteButton.setImageResource(R.drawable.ic_favourite_empty);
                    Toast.makeText(requireContext().getApplicationContext(),"Place deleted from database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext().getApplicationContext(),"Place unable to be deleted from database.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setUpFragmentUi(String distanceS, String durationS){
        duration.setText(durationS);
        distance.setText(distanceS);
    }

    public void setUpFragmentUiAddress(String destLocationS){
        //Log.d("TitleUpload", destLocationS);
        destLocation.setText(destLocationS);
    }

    public void setUpFragmentUiFavourite(){
        //Log.d("TitleUpload", destLocationS);
        favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_full);
    }
    //Method to add  place to favourite places in Firebase according to user
    public void addPlaceToFirebase(PlacesModel placesModel){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String placeID = databaseReference.push().getKey();
                placesModel.setPlaceID(placeID);
                databaseReference.child("FavouritePlaces").child(placeID).setValue(placesModel);
                Toast.makeText(requireContext().getApplicationContext(),"Place added to database",Toast.LENGTH_LONG).show();
                favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_full);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(requireContext().getApplicationContext(),"Failed to upload to database",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        /*
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }

         */
        setUpFragmentUiAddress(place.getName());
    }
}