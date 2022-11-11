package com.company.opsc_south_side_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment  {

    //Lesedi

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(user.getUid());
    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Vcz171LR1EfrkfpNBkxz6wzp6fF3");
    TextView phone, email, about, name;
    TextView settings, favourites;
    MaterialToolbar backToMap;

    //TODO: Start designing profile page
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        phone = view.findViewById(R.id.phoneTxt);
        email = view.findViewById(R.id.emailTxt);
        about = view.findViewById(R.id.aboutTxt);
        name = view.findViewById(R.id.profileNameTxt);
        settings = view.findViewById(R.id.settingsTxt);
        favourites = view.findViewById(R.id.favouritesTxt);
        backToMap = view.findViewById(R.id.profileToolbar);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //name = (TextView) snapshot.child("name").getValue();
                User user = snapshot.getValue(User.class);
                name.setText( user.getName());

               // phone = (TextView) snapshot.child("phoneNumber").getValue();
                phone.setText( user.getPhoneNumber());

                //email = (TextView) snapshot.child("email").getValue();
                email.setText( user.getEmail());

                //about = (TextView) snapshot.child("about").getValue();
                about.setText(user.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 = new settingsFragment();
                //Developers, 2021)
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewWhere, fragment1).setReorderingAllowed(true).commit();
            }
        });
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment2 = new favouritesFragment();
                //Developers, 2021)
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewWhere, fragment2).setReorderingAllowed(true).commit();
            }
        });
        //TODO: Make this go to the map activity/fragment
        backToMap.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment3 = new profileFragment();
                //Developers, 2021)
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewWhere, fragment3).commit();
            }
        });


        // Inflate the layout for this fragment
        return view;

    }


}