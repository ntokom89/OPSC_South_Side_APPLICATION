package com.company.opsc_south_side_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingsFragment extends Fragment {
    //TODO: In settings fragment, figure out how to change the units of measure

    //Lesedi

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toolbar backToProfile;
    TextView displayUser;
    String userDisplayName; //Holds user name from database to pass it to the name textView

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
                int transaction = getChildFragmentManager().beginTransaction()
                        .replace(R.id.settingsFragment, fragment1).commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    //Going to edit profile fragment when Edit Profile is clicked
    public void replaceWithEdit(){
        Fragment fragment1 = new editProfileFragment();
        //Developers, 2021)
        int transaction = getChildFragmentManager().beginTransaction()
                .replace(R.id.settingsFragment, fragment1).commit();
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
    }
}