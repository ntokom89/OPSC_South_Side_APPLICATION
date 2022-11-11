package com.company.opsc_south_side_application.profileFragments;

import static com.company.opsc_south_side_application.MainActivity.landmarkPreference;
import static com.company.opsc_south_side_application.MainActivity.mGoogleMap;
import static com.company.opsc_south_side_application.MainActivity.metric;
import static com.company.opsc_south_side_application.MainActivity.placesModelsList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.opsc_south_side_application.LoginRegistrationActivity;
import com.company.opsc_south_side_application.MainActivity;
import com.company.opsc_south_side_application.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingsFragment extends Fragment {
    //TODO: In settings fragment, figure out how to change the units of measure

    //Lesedi

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(user.getUid());
    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()
    );
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MaterialToolbar backToProfile;
    TextView displayUser;
    RadioGroup radioGroup;
    RadioButton buttonSelectedMetric;
    String userDisplayName; //Holds user name from database to pass it to the name textView
    TextView edit;
    TextView logout;
    Spinner spinnerLandmarks;
    int count = 0;

    public settingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static settingsFragment newInstance(String param1, String param2) {
        settingsFragment fragment = new settingsFragment();
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


        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        displayUser = view.findViewById(R.id.userLoggedIn);
        radioGroup = view.findViewById(R.id.unitsRadioGroup);
        edit = view.findViewById(R.id.goToEdit);
        logout = view.findViewById(R.id.logOut);
        spinnerLandmarks = view.findViewById(R.id.spinnerLandmark);

        setUpSpinner();
        if(metric.equals("metric")){
            buttonSelectedMetric = view.findViewById(R.id.metricRadioBtn);
            buttonSelectedMetric.toggle();
        }else if(metric.equals("imperial")){
            buttonSelectedMetric = view.findViewById(R.id.imperialRadioBtn);
            buttonSelectedMetric.toggle();
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 = new editProfileFragment();
                //Developers, 2021)
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewWhere, fragment1).commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOutUserFunction();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //method to check radiogroup if there are any changes on each button
            //https://firebase.google.com/docs/database/admin/save-data
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                buttonSelectedMetric = view.findViewById(selectedID);
                HashMap selectedUnitHashMap = new HashMap();

                selectedUnitHashMap.put("distanceUnit", buttonSelectedMetric.getText().toString().toLowerCase());
                //... set the value in the email field to the email entered in the edit text
                database.updateChildren(selectedUnitHashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getContext().getApplicationContext(), "Distance Unit updated",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext().getApplicationContext(), "Distance Unit not able to be updated",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDisplayName = (String) snapshot.child("name").getValue();
                displayUser.setText(userDisplayName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backToProfile = view.findViewById(R.id.settingsToolbar);
        backToProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 = new profileFragment();
                //Developers, 2021)
                 getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewWhere, fragment1).commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    //The method to set up the spinner and its methods
    //https://www.digitalocean.com/community/tutorials/android-spinner-drop-down-list  Anupam Chugh
    public void setUpSpinner(){

        List<String> landmarks = new ArrayList<String>();

        //List<String> landmarks = new ArrayList<String>();
        landmarks.add("Gas Station");
        landmarks.add("Restaurant");
        landmarks.add("Museum");
        landmarks.add("Park");
        landmarks.add("Supermarket");
        landmarks.add("None");

        spinnerLandmarks.setPrompt(landmarkPreference);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext().getApplicationContext(), android.R.layout.simple_spinner_item, landmarks);
        Toast.makeText(getContext().getApplicationContext(), "landmark preference is " + landmarkPreference,Toast.LENGTH_LONG).show();
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerLandmarks.setAdapter(dataAdapter);
        spinnerLandmarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String landmark= landmarks.get(i);
                if(landmark.equals(landmarkPreference) || count == 0){
                    count++;
                }else{
                    HashMap selectedUnitHashMap = new HashMap();

                    selectedUnitHashMap.put("landmarkPreference", landmark);
                    //... set the value in the email field to the email entered in the edit text
                    database.updateChildren(selectedUnitHashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            placesModelsList.clear();
                            mGoogleMap.clear();
                            Toast.makeText(getContext().getApplicationContext(), "landmark preference  updated",Toast.LENGTH_LONG).show();
                            //count++;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext().getApplicationContext(), "landmark preference  not able to be updated",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //Method to signout the user from the application
    private void SignOutUserFunction() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext().getApplicationContext(), LoginRegistrationActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    /*
    //Going to edit profile fragment when Edit Profile is clicked
    public void replaceWithEdit(){
        Fragment fragment1 = new editProfileFragment();
        //Developers, 2021)
        getChildFragmentManager().beginTransaction()
                .replace(R.id.settingsFragment, fragment1).commit();
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
    }

     */
}