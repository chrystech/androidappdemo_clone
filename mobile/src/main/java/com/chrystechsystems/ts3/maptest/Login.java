package com.chrystechsystems.ts3.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    static String username = "";
    static String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Code for guest button: skips to initial display (screen where you enter bldg and rm info)
        Button guestButton = findViewById(R.id.guestButton);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,InitialDisplay.class));
            }
        });

        // Code for register button: sends user to registration page
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });

        // When mail icon (floating action button/fab) is clicked on the login page this code is executed: code should check if passed-in credentials —
        // from username and password textviews (text input fields in activity_login.xml/content_login.xml) are valid by querying API
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = ((TextView) findViewById(R.id.username)).getText().toString();
                password = ((TextView) findViewById(R.id.password)).getText().toString();

                // TODO query API asking whether username and password pair exist

                Snackbar.make(view, "Credentials do not exist", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
