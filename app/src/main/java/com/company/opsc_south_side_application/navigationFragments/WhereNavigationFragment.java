package com.company.opsc_south_side_application.navigationFragments;

import static android.content.ContentValues.TAG;

import static com.company.opsc_south_side_application.MainActivity.buttonWhere;
import static com.company.opsc_south_side_application.MainActivity.favouritePlacesModelsList;
import static com.company.opsc_south_side_application.MainActivity.fragmentType;
import static com.company.opsc_south_side_application.MainActivity.getDirectionsUrl;
import static com.company.opsc_south_side_application.MainActivity.metric;
import static com.company.opsc_south_side_application.MainActivity.placesModelsList;
import static com.company.opsc_south_side_application.MainActivity.profileButton;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.company.opsc_south_side_application.MainActivity;
import com.company.opsc_south_side_application.R;
import com.company.opsc_south_side_application.adapters.whereplaceAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WhereNavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhereNavigationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LatLng originWhere;
    private LatLng destWhere;
    private Button buttonStartNav;
    private ImageButton backButton;
    private RecyclerView recyclerViewPlaces;
    private RecyclerView RecyclerViewFavouritesPlaces;

    public WhereNavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhereNavigationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhereNavigationFragment newInstance(String param1, String param2) {
        WhereNavigationFragment fragment = new WhereNavigationFragment();
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

    //method to create all UI components in fragments
    //https://stackoverflow.com/questions/41345987/how-to-use-placeautocompletefragment-widget-in-a-fragment Jordon
    //https://stackoverflow.com/questions/21824542/android-scrollview-inside-another-scrollview-doesnt-scroll Volodymyr Kulyk
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_where_navigation, container, false);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_OPSC_South_Side_Application);
        AutocompleteSupportFragment autocompleteFragmentStart = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autoCompleteOriginWhere);
        AutocompleteSupportFragment autocompleteFragmentDestination = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autoCompleteDestinationWhere);

        buttonStartNav = view.findViewById(R.id.buttonNavStart);
        backButton = view.findViewById(R.id.imageButtonBackMap);
        recyclerViewPlaces = view.findViewById(R.id.recyclerViewNearbyPlaces);
        RecyclerViewFavouritesPlaces = view.findViewById(R.id.recyclerViewFavourites);

        autocompleteFragmentStart.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

        autocompleteFragmentDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

        //Method to start navigation and give directions for the user.
        //https://www.youtube.com/watch?v=wRDLjUK8nyU The Code City
        buttonStartNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(originWhere != null&& destWhere != null){
                    URL urlConnection;
                    String url;
                    try {
                        url = getDirectionsUrl(originWhere, destWhere, null,metric);
                        fragmentType = "Where Nav";
                        urlConnection = new URL(url);
                        MainActivity main = new MainActivity();
                        main.impelementFetchDirection(urlConnection);
                        //new MainActivity.fetchDirectionsData().execute(urlConnection);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(originWhere == null){
                        Toast.makeText(getContext().getApplicationContext(),"Please enter your original starting location",Toast.LENGTH_SHORT).show();
                    }

                    if(destWhere == null){
                        Toast.makeText(getContext().getApplicationContext(),"Please enter your destination location",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //backbutton to remove the fragment
        //https://developer.android.com/guide/fragments/transactions
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialog d = getDialog();
                //d.dismiss();
                getParentFragmentManager().beginTransaction().remove(WhereNavigationFragment.this).commit();
                buttonWhere.setVisibility(View.VISIBLE);
                profileButton.setVisibility(View.VISIBLE);
            }
        });

        autocompleteFragmentStart.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                originWhere = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);


            }
        });

        autocompleteFragmentDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destWhere = new LatLng(Objects.requireNonNull(place.getLatLng()).latitude,place.getLatLng().longitude);
            }
        });

        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        whereplaceAdapter adapter = new whereplaceAdapter(placesModelsList);

        recyclerViewPlaces.setAdapter(adapter);


        RecyclerViewFavouritesPlaces.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        whereplaceAdapter adapterfav = new whereplaceAdapter(favouritePlacesModelsList);

        RecyclerViewFavouritesPlaces.setAdapter(adapterfav);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}