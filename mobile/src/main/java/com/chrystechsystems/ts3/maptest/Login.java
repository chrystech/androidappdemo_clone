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

        // Code for guest button: just skips to initial display (screen where you enter bldg and rm info)
        Button guestButton = findViewById(R.id.guestButton);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Login.this,InitialDisplay.class));
                }catch(Exception E){
                    Snackbar.make(view,"Who knows what happened!",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //username = ((TextView) findViewById(R.id.username)).getText().toString();

                Snackbar.make(view, "Credentials do not exist", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
