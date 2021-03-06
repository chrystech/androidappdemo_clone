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

    public Beacon(int id){
        getBeacon(id);
    }
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

    Beacon systemBeacon = null;
    private Beacon getBeacon(int id) {
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
    public LatLng getCoordinates(){return new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));}
    public int getFloor(){return floor;}
    public Beacon getCurrentBeacon(){return currentBeacon;}
    public Beacon getTargetBeacon(){return targetBeacon;}
}
