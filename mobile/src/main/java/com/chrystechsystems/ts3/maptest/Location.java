package com.chrystechsystems.ts3.maptest;

import com.google.android.gms.maps.model.LatLng;

public interface Location {
    int getID();
    String getBuilding();
    int getRoom();
    String getLocName();
    String getLatitude();
    String getLongitude();
    LatLng getCoordinates();
    int getFloor();
}
