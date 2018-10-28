package com.chrystechsystems.ts3.maptest;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wear.widget.SwipeDismissFrameLayout;
import android.support.wearable.activity.WearableActivity;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.location.Location.distanceBetween;

public class MapsActivity extends WearableActivity implements OnMapReadyCallback, LocationListener {
    public static Beacon systemBeacon = Beacon.systemBeacon;
    public static LatLng lastRecordedLocation = new LatLng(0,0);
    private final int R_CODE_LOC_FINE_PERM = 1;
    LocationManager locMGR;
    /**
     * Map is initialized when it's fully loaded and ready to be used.
     *
     * @see #onMapReady(com.google.android.gms.maps.GoogleMap)
     */
    private GoogleMap mMap;
    final void setBeacon(Beacon beacon) {
        systemBeacon = beacon;
    }

    Beacon getBeacon(String buildingId, int roomNumber) {
        final String buildingTop = buildingId;
        final int roomTop = roomNumber;

        AsyncTask.execute(new Runnable() {
            //final int id2 = id3;
            final String building = buildingTop;
            final int room = roomTop;

            @Override
            public void run() {
                // Create URL
                try {
                    //URL githubEndpoint = new URL("https://api.github.com/");
                    URL endPoint = new URL("http://ts3.chrystechsystems.com/api/ts3teamapikey1/beacon/find/building/" + building + "/room/" + room);
                    // Create connection
                    HttpURLConnection myConnection =
                            (HttpURLConnection) endPoint.openConnection();//.openConnection();


                    myConnection.setRequestProperty("User-Agent", "Fox_sMap-v0.1");


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
                        System.err.println("Couldn't get a successful response from the server, is the server down?");
                    }


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

    /**
     * When Device requests permissions, handle them?
     * @param requestCode request code for permission request
     * @param permissions permissions being requested
     * @param grantResults did the user grant these permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == R_CODE_LOC_FINE_PERM) {
            if (permissions.length == 1 &&
                    permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
                System.err.println("Error requesting permissions from user... Application will not work without permissions.");
            }
        }

    }
    final int MapReasonableZoom = 17;
    final int MapPadding = 100;

    /**
     * Get a good set of bounds for the viewport based on your current location and the location of the beacon target
     * @param location Your current device location
     * @param beacon LatLng of the beacon target
     * @return LatLngBounds object specifying correct latitude and longitude of diagonal corners for viewport based on location of the beacon and your device
     */
    LatLngBounds goodBounds(Location location, LatLng beacon){
        return new LatLngBounds(
                new LatLng(Math.min(location.getLatitude(),beacon.latitude),Math.min(location.getLongitude(),beacon.longitude)),//northeast
                new LatLng(Math.max(location.getLatitude(),beacon.latitude),Math.max(location.getLongitude(),beacon.longitude))//southwest
        );//padding
    }

    /**
     * Get a good polyline option set for the current instructions
     * @param location Your current device location
     * @param beacon LatLng of the beacon target
     * @return PolylineOptions options specifying the correct indicators for the polyline connecting current location and beacon location
     */
    PolylineOptions goodPolyLine(Location location, LatLng beacon){
        System.err.println(location.getAccuracy());
        PolylineOptions p = new PolylineOptions().add(new LatLng(location.getLatitude(),location.getLongitude()),beacon);
        float[] e = new float[3];
        distanceBetween(location.getLatitude(),location.getLongitude(),beacon.latitude,beacon.longitude, e);
        if(e[0] > 100) p.color(Color.RED);
        else if (e[0] > 50) p.color(Color.rgb(255,0,255));
        else p.color(Color.BLUE);
        return p;
    }
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // Enables always on.
        setAmbientEnabled();

        setContentView(R.layout.activity_maps);

        final SwipeDismissFrameLayout swipeDismissRootFrameLayout =
                (SwipeDismissFrameLayout) findViewById(R.id.swipe_dismiss_root_container);
        final FrameLayout mapFrameLayout = (FrameLayout) findViewById(R.id.map_container);

        // Enables the Swipe-To-Dismiss Gesture via the root layout (SwipeDismissFrameLayout).
        // Swipe-To-Dismiss is a standard pattern in Wear for closing an app and needs to be
        // manually enabled for any Google Maps Activity. For more information, review our docs:
        // https://developer.android.com/training/wearables/ui/exit.html
        swipeDismissRootFrameLayout.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                // Hides view before exit to avoid stutter.
                layout.setVisibility(View.GONE);
                finish();
            }
        });

        // Adjusts margins to account for the system window insets when they become available.
        swipeDismissRootFrameLayout.setOnApplyWindowInsetsListener(
                new View.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                        insets = swipeDismissRootFrameLayout.onApplyWindowInsets(insets);

                        FrameLayout.LayoutParams params =
                                (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                        // Sets Wearable insets to FrameLayout container holding map as margins
                        params.setMargins(
                                insets.getSystemWindowInsetLeft(),
                                insets.getSystemWindowInsetTop(),
                                insets.getSystemWindowInsetRight(),
                                insets.getSystemWindowInsetBottom());
                        mapFrameLayout.setLayoutParams(params);

                        return insets;
                    }
                });

        // Obtain the MapFragment and set the async listener to be notified when the map is ready.
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Map is ready to be used.
        mMap = googleMap;

        // Inform user how to close app (Swipe-To-Close).
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getApplicationContext(), R.string.intro_text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        // Adds a marker in Sydney, Australia and moves the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    @Override
    public void onLocationChanged(android.location.Location location) {
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
        }else {
            //Logger.getLogger("Fox's Map").log(Level.ALL,"Location Changed");
            System.err.println("Location Changed");
            mMap.clear();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            lastRecordedLocation = latLng;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, MapReasonableZoom);//17

            mMap.animateCamera(cameraUpdate);
            //locMGR.removeUpdates(this);
            if (systemBeacon == null)
                systemBeacon = getBeacon(InitialDisplay.building, InitialDisplay.room);
            if (systemBeacon != null) {
                LatLng beacon = systemBeacon.getCoordinates();
                mMap.addMarker(new MarkerOptions().position(beacon).title(systemBeacon.getLocName()));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(beacon));
                mMap.addPolyline(goodPolyLine(location, beacon));//new PolylineOptions().add(new LatLng(location.getLatitude(),location.getLongitude()),beacon));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 17));
                //LatLng halfway = new LatLng((location.getLatitude() + beacon.latitude)/2,(location.getLongitude() + beacon.longitude)/2);
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halfway, (float)(MapReasonableZoom-Math.max(Math.abs(location.getLatitude()-beacon.latitude)*MapZoomDifferential,Math.abs(location.getLongitude()-beacon.longitude)*MapZoomDifferential))));
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                        goodBounds(location, beacon), MapPadding
                ));
                float e[] = new float[3];
                distanceBetween(location.getLatitude(), location.getLongitude(), beacon.latitude, beacon.longitude, e);
                System.err.println("Accuracy at: " + location.getAccuracy());
                if(location.getAccuracy() > 2 && e[0] <= 50){
                    System.err.println("Client is in or around building target");
                    startActivity(new Intent(MapsActivity.this,IndoorTracking.class));
                }
            } else {
                try {
                    Thread.sleep(2000);
                    if (systemBeacon != null) {
                        LatLng beacon = systemBeacon.getCoordinates();
                        mMap.addMarker(new MarkerOptions().position(beacon).title(systemBeacon.getLocName()));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beacon, 17));
                        mMap.addPolyline(goodPolyLine(location, beacon));//new PolylineOptions().add(new LatLng(location.getLatitude(),location.getLongitude()),beacon));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 17));
                        //LatLng halfway = new LatLng((location.getLatitude() + beacon.latitude)/2,(location.getLongitude() + beacon.longitude)/2);
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(halfway, (float)(MapReasonableZoom-Math.max(Math.abs(location.getLatitude()-beacon.latitude)*MapZoomDifferential,Math.abs(location.getLongitude()-beacon.longitude)*MapZoomDifferential))));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                                goodBounds(location, beacon), MapPadding
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

