package com.company.opsc_south_side_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.company.opsc_south_side_application.loginRegsiterFragments.LoginFragment;

public class LoginRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewLoginRegsiter,new LoginFragment()).setReorderingAllowed(true).commit();

    }
}