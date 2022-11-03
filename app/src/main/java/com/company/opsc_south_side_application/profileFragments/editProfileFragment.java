package com.company.opsc_south_side_application.profileFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.opsc_south_side_application.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editProfileFragment extends Fragment {

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

    Toolbar back;
    EditText name, about, homeAddress, phoneNum, email;
    Button save;
    View editProfile;

    String userName, userAbout, userAddress, userPhone, userEmail;

    public editProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static editProfileFragment newInstance(String param1, String param2) {
        editProfileFragment fragment = new editProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        name = view.findViewById(R.id.nameEditTextEditProfile);
        about = view.findViewById(R.id.aboutEditTextEditProfile);
        homeAddress = view.findViewById(R.id.addressEditTextEditProfile);
        phoneNum = view.findViewById(R.id.numberEditTextEditProfile);
        email = view.findViewById(R.id.emailEditTextEditProfile);

        save = view.findViewById(R.id.saveBtn);

        editProfile = view.findViewById(R.id.editProfileView);

        back = view.findViewById(R.id.editProfileToolbar);
        //back.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back);
        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starting Settings fragment
                Fragment fragment1 = new settingsFragment();
                int transaction = getChildFragmentManager().beginTransaction()
                        .replace(R.id.editProfileView, fragment1).commit();
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = (String) snapshot.child("name").getValue();
                name.setText(userName);

                userAbout = (String) snapshot.child("about").getValue();
                about.setText(userAbout);

                userAddress = (String) snapshot.child("address").getValue();
                homeAddress.setText(userAddress);

                userPhone = (String) snapshot.child("phoneNumber").getValue();
                phoneNum.setText(userPhone);

                userEmail = (String) snapshot.child("email").getValue();
                email.setText(userEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Inflate the layout for this fragment
        return view;
    }
    //This runs when the Save button is clicked (see on click listener for button in layout)
    public void update(View view){
        if(nameChanged() || aboutChanged() || addressChanged() || numberChanged() || emailChanged()) {
            Toast.makeText(getContext().getApplicationContext(), "Details updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext().getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
        }
    }

    //(see Update Data Firebase Android Studio Edit Profile Android Studio Firebase Firebase realtime database, 2020)
    //Logic for the email changed is the same for all the fields changed methods after
    //https://firebase.google.com/docs/database/admin/save-data
    private boolean emailChanged() {
        //if email from database does not match email entered in edit text...
        if(!userEmail.equals(email.getText().toString())){
            HashMap emailHash = new HashMap();

            emailHash.put("email", email.getText().toString());
            //... set the value in the email field to the email entered in the edit text
            database.child(userEmail).updateChildren(emailHash).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(getContext().getApplicationContext(), "Email updated",Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        } else {
            return false;
        }

    }

    private boolean numberChanged() {
        if(!userPhone.equals(phoneNum.getText().toString())){
            database.child(userPhone).child("phoneNumber").setValue(phoneNum.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean addressChanged() {
        if(!userAddress.equals(homeAddress.getText().toString())){
            database.child(userAddress).child("address").setValue(homeAddress.getText().toString());
            return true;
        } else {
            return false;
        }

    }

    private boolean aboutChanged() {
        if(!userAbout.equals(about.getText().toString())){
            database.child(userAbout).child("about").setValue(about.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean nameChanged() {

        if(!userName.equals(name.getText().toString())){
            database.child(userName).child("name").setValue(name.getText().toString());
            return true;
        } else {
            return false;
        }
    }
}