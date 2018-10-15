package com.chrystechsystems.ts3.maptest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.JsonReader;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.location.Location.distanceBetween;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    public static Beacon systemBeacon = Beacon.systemBeacon;

    private GoogleMap mMap;
    LocationManager locMGR;
    private final int R_CODE_LOC_FINE_PERM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    void readJson() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Create URL
                try {
                    //URL githubEndpoint = new URL("https://api.github.com/");
                    URL endPoint = new URL("http://ts3.chrystechsystems.com/api/ts3teamapikey1/beacon/123456");
                    // Create connection
                    HttpURLConnection myConnection =
                            (HttpURLConnection) endPoint.openConnection();//.openConnection();


                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");


                    //myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    myConnection.setRequestProperty("Accept", "application/json");
                        /*myConnection.setRequestProperty("Contact-Me",
                                "hathibelagal@example.com");*/
                    if (myConnection.getResponseCode() == 200) {
                        // Success
                        // Further processing here
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginArray();

                        while (jsonReader.hasNext()) { // Loop through all keys
                            //System.out.println(jsonReader.nextString());
                            jsonReader.beginObject();
                            //if(jsonReader.)
                            String value = "";
                            while (jsonReader.hasNext()) {
                                String key = jsonReader.nextName();
                                System.out.println(key);
                                //if (key.equals("name")) {
                                //String value = jsonReader.nextString();
                                value += jsonReader.nextString() + "\n";
                                /*Snackbar.make(findViewById(R.id.toolbar), value, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();*/
                                //}
                                //else {jsonReader.skipValue();}
                            }
                            jsonReader.endObject();
                        }

                        jsonReader.endArray();
                    } else {
                        // Error handling code goes here
                    }


                } catch (MalformedURLException MUE) {
                    MUE.printStackTrace();
                } catch (IOException IOE) {
                    IOE.printStackTrace();
                }
            }
        });
    }


    final void setBeacon(Beacon beacon) {
        systemBeacon = beacon;
    }

    Beacon getBeacon(int id) {
        final int id3 = id;


        AsyncTask.execute(new Runnable() {
            final int id2 = id3;

            @Override
            public void run() {
                // Create URL
                try {
                    //URL githubEndpoint = new URL("https://api.github.com/");
                    URL endPoint = new URL("http://ts3.chrystechsystems.com/api/ts3teamapikey1/beacon/" + id2);
                    // Create connection
                    HttpURLConnection myConnection =
                            (HttpURLConnection) endPoint.openConnection();//.openConnection();


                    myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");


                    //myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    myConnection.setRequestProperty("Accept", "application/json");
                        /*myConnection.setRequestProperty("Contact-Me",
                                "hathibelagal@example.com");*/
                    if (myConnection.getResponseCode() == 200) {
                        // Success
                        // Further processing here
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        systemBeacon = new Beacon(jsonReader);
                    } else {
                        // Error handling code goes here
                    }


                } catch (MalformedURLException MUE) {
                    MUE.printStackTrace();
                } catch (IOException IOE) {
                    IOE.printStackTrace();
                }
            }
        });
        return systemBeacon;
    }

    void requestMapPerms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            String[] permsToRequest = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permsToRequest, R_CODE_LOC_FINE_PERM);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == R_CODE_LOC_FINE_PERM) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }

    }
    final int MapReasonableZoom = 17;
    final int MapPadding = 100;

    LatLngBounds goodBounds(Location location, LatLng beacon){
        return new LatLngBounds(
                new LatLng(Math.min(location.getLatitude(),beacon.latitude),Math.min(location.getLongitude(),beacon.longitude)),//northeast
                new LatLng(Math.max(location.getLatitude(),beacon.latitude),Math.max(location.getLongitude(),beacon.longitude))//southwest
        );//padding
    }

    PolylineOptions goodPolyLine(Location location, LatLng beacon){
        PolylineOptions p = new PolylineOptions().add(new LatLng(location.getLatitude(),location.getLongitude()),beacon);
        float[] e = new float[3];
        distanceBetween(location.getLatitude(),location.getLongitude(),beacon.latitude,beacon.longitude, e);
        if(e[0] > 100) p.color(Color.RED);
        else if (e[0] > 20) p.color(Color.rgb(255,0,255));
        else p.color(Color.BLUE);
        return p;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Logger.getLogger("FoxMap").log(Level.ALL,"TEST");
        System.out.println("Test2");
        mMap = googleMap;
        requestMapPerms();
        locMGR = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnMyLocationClickListener(this);

        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestMapPerms();
            return;
        }
        android.location.Location location = locMGR.getLastKnownLocation(locMGR
                .getBestProvider(criteria, false));
        System.out.println("System Beacon: " + systemBeacon);
        if(systemBeacon == null)
            getBeacon(123456);
        if(systemBeacon != null) {
            LatLng beacon = systemBeacon.getCoordinates();
            //LatLng halfway = new LatLng((location.getLatitude() + beacon.latitude)/2,(location.getLongitude() + beacon.longitude)/2);
            mMap.addMarker(new MarkerOptions().position(beacon).title(systemBeacon.getLocName()));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(beacon));

            mMap.addPolyline(goodPolyLine(location,beacon));//new PolylineOptions().add(new LatLng(location.getLatitude(),location.getLongitude()),beacon));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 30));//17
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halfway, (float)(MapReasonableZoom-Math.max(Math.abs(location.getLatitude()-beacon.latitude)*MapZoomDifferential,Math.abs(location.getLongitude()-beacon.longitude)*MapZoomDifferential))));
            locMGR.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,3,this);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    goodBounds(location, beacon),MapPadding
            ));
        } else {
            try {
                while(systemBeacon == null){
                Thread.sleep(2000);
                if(systemBeacon != null) {
                    LatLng beacon = systemBeacon.getCoordinates();
                    mMap.addMarker(new MarkerOptions().position(beacon).title(systemBeacon.getLocName()));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beacon, 17));

                    mMap.addPolyline(goodPolyLine(location,beacon));//new PolylineOptions().add(new LatLng(location.getLatitude(),location.getLongitude()),beacon));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 17));//17
                    //LatLng halfway = new LatLng((location.getLatitude() + beacon.latitude)/2,(location.getLongitude() + beacon.longitude)/2);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halfway, (float)(MapReasonableZoom-Math.max(Math.abs(location.getLatitude()-beacon.latitude)*MapZoomDifferential,Math.abs(location.getLongitude()-beacon.longitude)*MapZoomDifferential))));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                            goodBounds(location, beacon),MapPadding
                    ));
                    locMGR.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,3,this);


                } else {
                    LatLng sydney = new LatLng(-34, 151);
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Add a marker in Sydney and move the camera
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        //Logger.getLogger("Fox's Map").log(Level.ALL,"Location Changed");
        System.err.println("Location Changed");
        mMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, MapReasonableZoom);//17

        mMap.animateCamera(cameraUpdate);
        //locMGR.removeUpdates(this);
        if(systemBeacon == null)
            getBeacon(123456);
        if(systemBeacon != null) {
            LatLng beacon = systemBeacon.getCoordinates();
            mMap.addMarker(new MarkerOptions().position(beacon).title(systemBeacon.getLocName()));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(beacon));
            mMap.addPolyline(goodPolyLine(location,beacon));//new PolylineOptions().add(new LatLng(location.getLatitude(),location.getLongitude()),beacon));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 17));
            //LatLng halfway = new LatLng((location.getLatitude() + beacon.latitude)/2,(location.getLongitude() + beacon.longitude)/2);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halfway, (float)(MapReasonableZoom-Math.max(Math.abs(location.getLatitude()-beacon.latitude)*MapZoomDifferential,Math.abs(location.getLongitude()-beacon.longitude)*MapZoomDifferential))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    goodBounds(location, beacon),MapPadding
            ));
        } else {
            try {
                Thread.sleep(2000);
                if(systemBeacon != null) {
                    LatLng beacon = systemBeacon.getCoordinates();
                    mMap.addMarker(new MarkerOptions().position(beacon).title(systemBeacon.getLocName()));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beacon, 17));
                    mMap.addPolyline(goodPolyLine(location,beacon));//new PolylineOptions().add(new LatLng(location.getLatitude(),location.getLongitude()),beacon));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 17));
                    //LatLng halfway = new LatLng((location.getLatitude() + beacon.latitude)/2,(location.getLongitude() + beacon.longitude)/2);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halfway, (float)(MapReasonableZoom-Math.max(Math.abs(location.getLatitude()-beacon.latitude)*MapZoomDifferential,Math.abs(location.getLongitude()-beacon.longitude)*MapZoomDifferential))));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                            goodBounds(location, beacon),MapPadding
                    ));
                } else {
                    LatLng sydney = new LatLng(-34, 151);
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
