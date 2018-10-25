package com.chrystechsystems.ts3.maptest;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void beacon_WORKS() {
        Beacon x = new Beacon(123456,"4",349,"Computer Lab","30.546806","-87.216669",3);
        Beacon y = new Beacon(123456,"4",349,"Computer Lab","30.546806","-87.216669",3);
        assertEquals(x.getID(),y.getID());
        assertEquals(x.getBuilding(),y.getBuilding());
        assertEquals(x.getRoom(),y.getRoom());
        assertEquals(x.getFloor(),y.getFloor());
    }

}