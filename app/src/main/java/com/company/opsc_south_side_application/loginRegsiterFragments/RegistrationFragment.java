package com.company.opsc_south_side_application.loginRegsiterFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.opsc_south_side_application.R;
import com.company.opsc_south_side_application.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    EditText username,email,password;
    TextView signIn;
    Button register;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
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
        View view =  inflater.inflate(R.layout.fragment_registration, container, false);

        username = view.findViewById(R.id.editTextRegisterUsername);
        email = view.findViewById(R.id.editTextRegisterEmail);
        password = view.findViewById(R.id.editTextRegsiterPassword);
        register = view.findViewById(R.id.buttonSignUp);
        signIn = view.findViewById(R.id.textViewSignIn);
        firebaseAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(password.getText()) ||TextUtils.isEmpty(email.getText())||TextUtils.isEmpty(username.getText())){
                    if(TextUtils.isEmpty(password.getText())){
                        password.setError("Please enter a password");
                    }
                    if(TextUtils.isEmpty(email.getText())){
                        email.setError("Please enter a email");
                    }
                    if(TextUtils.isEmpty(username.getText())){
                        username.setError("Please enter a username");
                    }
                }else{
                    RegisterUserToFirebase(email.getText().toString(),password.getText().toString(),username.getText().toString());
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewLoginRegsiter,new LoginFragment()).setReorderingAllowed(true).commit();

            }
        });
        return view;
    }

    //Register user with email and password
    private void RegisterUserToFirebase(String email, String password, String name) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber("");
        user.setDistanceUnit("metric");
        user.setLandmarkPreference("None");
        user.setAddress("");
        user.setAbout("");
        user.setPoints(0);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                    String firebaseUser = firebaseAuth.getUid();
                    user.setUserID(firebaseAuth.getUid());
                    addToFirebase(user);
                    Log.d("userID",firebaseUser);
                    // = "0q89wT3EOGf0k1ostjHeqJ3eZIH3";
                    Toast.makeText(getContext().getApplicationContext(),"Registration successful",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext().getApplicationContext(),"Check your email or password",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Add user to Firebase
    private void addToFirebase(User user) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserID());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                //databaseReference.setValue(email);
                databaseReference.setValue(user);
                //databaseReference.child("password").setValue(password);
                Toast.makeText(getContext().getApplicationContext(),"User details added to database ",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext().getApplicationContext(),"Database error",Toast.LENGTH_LONG).show();
            }
        });

    }

}