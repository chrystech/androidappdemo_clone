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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Login extends AppCompatActivity {
    static String username = "";
    static String password = "";

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // The GoogleSignInOptions object. DEFAULT_SIGN_IN and requestEmail ask for basic user information and email respectively,
        // the string passed into requestIdToken is an OAuth 2.0 Client ID for the API/backend
        // backend probably has to be configured to accept the IdToken and use it to communicate with google-services for authentication
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("742671130780-nu8ksuf3k171iuepshq0pdufgdnq3tqd.apps.googleusercontent.com")
                .build();

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

        // mGoogleSignInClient = GoogleSignIn.getClient(activity:this, gso);
    }

}
