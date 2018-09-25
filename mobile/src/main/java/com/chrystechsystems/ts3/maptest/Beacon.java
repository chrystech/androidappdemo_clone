package com.chrystechsystems.ts3.maptest;

import android.util.JsonReader;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;


public class Beacon implements Location {
    int id, room, floor;
    String name, lat, lon, bldg;

    public Beacon(int id, int bldgnum, int room, String name, String lat, String lon, int floor){

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
}
