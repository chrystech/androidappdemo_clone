package com.chrystechsystems.ts3.maptest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.JsonReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.chrystechsystems.ts3.maptest", appContext.getPackageName());
    }
    @Test
    public void getDefaultBeacon(){
        //assertNotNull(Beacon.getBeacon("4",349));
        try {
            Beacon bob = new Beacon(new JsonReader(new StringReader("[\n" +
                    "  {\n" +
                    "    \"_id\": \"5b8c4ae64808a40502a80298\",\n" +
                    "    \"id\": 123456,\n" +
                    "    \"building\": \"4\",\n" +
                    "    \"room\": 349,\n" +
                    "    \"name\": \"Computer Lab\",\n" +
                    "    \"latitude\": \"30.546806\",\n" +
                    "    \"longitude\": \"-87.216669\",\n" +
                    "    \"floor\": 3,\n" +
                    "    \"__v\": 0\n" +
                    "  }\n" +
                    "]")));
            assertNotNull(bob);
        }catch(IOException IOE){
            assertNull(IOE);
        }
        //new Beacon(123456,"4",349,"Computer Lab", "30.546806","-87.216669",3);
    }
    @Test
    public void canGatherBeaconData(){
        assertNotNull(Beacon.getBeacon("4",349));
        System.out.println(Beacon.getBeacon("4",349).getCoordinates());
        assertNotNull(Beacon.getBeacon("4",349).getLocName());
    }

}
