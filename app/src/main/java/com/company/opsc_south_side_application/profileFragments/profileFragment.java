package com.company.opsc_south_side_application.profileFragments;

import static com.company.opsc_south_side_application.MainActivity.buttonWhere;
import static com.company.opsc_south_side_application.MainActivity.profileButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.opsc_south_side_application.R;
import com.company.opsc_south_side_application.models.User;
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
    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
    TextView phone, email, about, name, points;
    TextView settings, share, favourite;
    ImageButton backButton;

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
        points = view.findViewById(R.id.pointsTxt);
        share = view.findViewById(R.id.shareTxt);
        backButton = view.findViewById(R.id.imageButtonBackProfile);
        favourite = view.findViewById(R.id.favouritesTxt);
        //replaceWithSettings(view);

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

                points.setText(user.getPoints() + "");
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().remove(profileFragment.this).commit();
                buttonWhere.setVisibility(View.VISIBLE);
                profileButton.setVisibility(View.VISIBLE);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1 = new FavouriteListFragment();
                //Developers, 2021)
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewWhere, fragment1).setReorderingAllowed(true).commit();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Your body is here";
                String shareSub = "Your subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });


        // Inflate the layout for this fragment
        return view;

    }


}