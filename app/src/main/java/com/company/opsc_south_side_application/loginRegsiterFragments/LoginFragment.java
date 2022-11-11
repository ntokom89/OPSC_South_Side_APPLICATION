package com.company.opsc_south_side_application.loginRegsiterFragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.company.opsc_south_side_application.MainActivity;
import com.company.opsc_south_side_application.R;
import com.company.opsc_south_side_application.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference databaseReference;
    EditText email,password;
    Button login;
    TextView signUp;
    FirebaseAuth mAuth;
    private TextView textViewSignInGoogle;
    private GoogleSignInClient client;
    ActivityResultLauncher<Intent> activityResultLauncher;
    GoogleSignInOptions gso;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = null;
        try{
             view = inflater.inflate(R.layout.fragment_login, container, false);
        }catch(Exception e){
            Log.e("Exception handling", "onCreateView", e);
            throw e;
        }
        //View view = inflater.inflate(R.layout.fragment_login, container, false);

        registerActivityForGoogleSignIn();
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1044551788631-gcv5upuehok137kdpf8heo6jhn8eg130.apps.googleusercontent.com")
                .requestEmail().build();
        login = view.findViewById(R.id.buttonLogin);
        email = view.findViewById(R.id.editTextLoginEmail);
        password = view.findViewById(R.id.editTextLoginPassword);
        signUp = view.findViewById(R.id.textViewSignUp);
        textViewSignInGoogle = view.findViewById(R.id.textViewGoogleAccountLink);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(TextUtils.isEmpty(email.getText())) && !(TextUtils.isEmpty(password.getText()))){
                    Log.d("Passed through check","inputs not null");
                        signInWithAccount(email.getText().toString(), password.getText().toString());

                }else{
                    if(TextUtils.isEmpty(password.getText())){
                        password.setError("Please enter your password");
                    }
                    if(TextUtils.isEmpty(email.getText())){
                        email.setError("Please enter your email");
                    }

                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewLoginRegsiter,new RegistrationFragment()).setReorderingAllowed(true).commit();

            }
        });

        textViewSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });
        return view;
    }

    //Sign into Firebase with email and password
    private void signInWithAccount(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser userFirebase = mAuth.getCurrentUser();
                    String user = mAuth.getUid();
                    Log.d("userID",user);
                    // = "0q89wT3EOGf0k1ostjHeqJ3eZIH3";
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(user);
                    Toast.makeText(getContext().getApplicationContext() ,"Login successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext().getApplicationContext() ,"Check your email or password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void signInGoogle(){



        client = GoogleSignIn.getClient(getContext().getApplicationContext(),gso);
        Log.d("Google login","Get Client sign in reached");
        signIn();

    }

    private void signIn() {
        Intent signInIntent = client.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
        Log.d("Google login","activity result launcher launched");
    }

    //A new activivty to sign in for Google
    //https://www.codeproject.com/Articles/1113772/Adding-Google-Login-to-Android-App Andy Point
    public void registerActivityForGoogleSignIn(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        Log.d("GoogleLogin", "Activity result launcher");
                        Log.d("GoogleLogin", "result Code" + resultCode);
                        if(resultCode == RESULT_OK && data != null){

                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            firebaseSignedInWithGoogle(task);
                            Log.d("Google login", "Activity result firebase sign in");
                        }

                        if(data == null){
                            Log.d("GoogleLogin", "Data is null");
                        }
                    }
                });
    }

    //Check if google login is successful
    private void firebaseSignedInWithGoogle(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(getContext().getApplicationContext(), "Successfully Logged in", Toast.LENGTH_SHORT).show();
            //finish();
            firebaseGoogleAccount(account);

        } catch (ApiException e){
            e.printStackTrace();
            Toast.makeText(getContext().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //Sign in with Google Account
    //https://www.codeproject.com/Articles/1113772/Adding-Google-Login-to-Android-App Andy Point
    private void firebaseGoogleAccount(GoogleSignInAccount account) {

        AuthCredential authCredentials = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(authCredentials).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            FirebaseUser userGoogle = mAuth.getCurrentUser();
                            Query query = databaseReference.orderByChild("userID").equalTo(userGoogle.getUid());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.exists()){
                                        Intent i = new Intent(getContext().getApplicationContext(),MainActivity.class);
                                        startActivity(i);
                                    }else{
                                        User user = new User();
                                        user.setName(userGoogle.getEmail());
                                        user.setEmail(userGoogle.getEmail());
                                        user.setUserID(userGoogle.getUid());
                                        user.setPhoneNumber("");
                                        user.setDistanceUnit("metric");
                                        user.setLandmarkPreference("None");
                                        user.setPoints(0);
                                        user.setAbout("");
                                        user.setAddress("");
                                        databaseReference.child(user.getUserID()).setValue(user);
                                        Intent i = new Intent(getContext().getApplicationContext(),MainActivity.class);
                                        startActivity(i);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            //user.
                        }else{

                        }
                    }
                });
    }
}