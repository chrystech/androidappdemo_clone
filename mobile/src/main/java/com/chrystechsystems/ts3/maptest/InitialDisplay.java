package com.chrystechsystems.ts3.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class InitialDisplay extends AppCompatActivity {
    static String building = "";
    static int room = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                building = ((TextView) findViewById(R.id.Building)).getText().toString();
                try {
                    room = Integer.parseInt(((TextView) findViewById(R.id.RoomNum)).getText().toString());
                    startActivity(new Intent(InitialDisplay.this,MapsActivity.class));
                }catch(Exception E){
                    Snackbar.make(view,"Room Number Is Likely Not A Number!",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
            }
        });
    }

}
