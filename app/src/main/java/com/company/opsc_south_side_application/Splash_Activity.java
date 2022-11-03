package com.company.opsc_south_side_application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class Splash_Activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(Splash_Activity.this, MainActivity.class));
                    finish();
                } else {


                    startActivity(new Intent(Splash_Activity.this, LoginRegistrationActivity.class));
                    finish();
                }


            }
        }, 4000);

    }
}