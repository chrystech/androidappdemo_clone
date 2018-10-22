package com.chrystechsystems.ts3.maptest;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Beacon implements Location {
    private static Beacon currentBeacon,targetBeacon = null;

    private int id, room, floor;
    private String name, lat, lon, bldg;

    /*public Beacon(int id){
        System.out.println("new Beacon");
        getBeacon(id);
    }*/
    public Beacon(int id, String bldg, int room, String name, String lat, String lon, int floor){
        this.id = id;
        this.bldg = bldg;
        this.room = room;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.floor = floor;
    }
    public Beacon(JsonReader jsonReader) throws IOException{
        jsonReader.beginArray();

        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            String value = "";
            while(jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                System.out.println(key);
                if(key.equalsIgnoreCase("id")){
                    id=jsonReader.nextInt();
                } else if (key.equalsIgnoreCase("building")) {
                    bldg = jsonReader.nextString();
                }else if (key.equalsIgnoreCase("room")) {
                    room = jsonReader.nextInt();
                }else if (key.equalsIgnoreCase("name")) {
                    name = jsonReader.nextString();
                }else if (key.equalsIgnoreCase("latitude")) {
                    lat = jsonReader.nextString();
                }else if (key.equalsIgnoreCase("longitude")) {
                    lon = jsonReader.nextString();
                }else if (key.equalsIgnoreCase("floor")) {
                    floor = jsonReader.nextInt();
                }
                else {
                    value += jsonReader.nextString() + "\n";
                }
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();
        currentBeacon = this;
    }

    public static Beacon systemBeacon = null;
    public static Beacon getBeacon(String bldg, int rm) {
        //System.out.println("Getting beacon with id: " + id);
        //final int id3 = id;
        final String bldgTop = bldg;
        final int rmTop = rm;

        AsyncTask.execute(new Runnable() {
            //final int id2 = id3;
            final String building = bldgTop;
            final int room = rmTop;
            @Override
            public void run() {
                System.out.println("In ASYNC Run Loop");
                // Create URL
                try {
                    //URL githubEndpoint = new URL("https://api.github.com/");
                    URL endPoint = new URL("http://ts3.chrystechsystems.com/api/ts3teamapikey1/beacon/find/building/" + building + "/room/" + room);
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
                        System.err.println("Server request failed");
                    }


                } catch (IOException IOE) {
                    IOE.printStackTrace();
                }
                System.out.println("Leaving Run Loop");
            }
        });

        System.out.println("Returning the beacon");
        int timeout = 10;//in seconds
        int i = 0;
        while(systemBeacon == null && i < timeout){try{Thread.sleep(1000);i++;}catch(InterruptedException IE){IE.printStackTrace();}}
        System.out.println(systemBeacon.getCoordinates());
        return systemBeacon;
    }







    public int getID(){
        return id;
    }
    public String getBuilding(){
        return bldg;
    }
    public int getRoom(){
        return room;
    }
    public String getLocName(){return name;}
    public String getLatitude(){return lat;}
    public String getLongitude(){return lon;}
    public LatLng getCoordinates(){if(lat != null && lon != null)return new LatLng(Double.parseDouble(getLatitude()),Double.parseDouble(getLongitude()));else return null;}
    public int getFloor(){return floor;}
    public Beacon getCurrentBeacon(){return currentBeacon;}
    public Beacon getTargetBeacon(){return targetBeacon;}
}
