package com.chrystechsystems.ts3.maptest;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

public class BearingTests {
    @Test
    public void BearingTestWorks(){
        Assert.assertEquals(IndoorTracking.getBearing(new LatLng(40.76, -73.984), new LatLng(40.75921, -73.22951)), 90.08, 3);
        Assert.assertEquals(IndoorTracking.getBearing(new LatLng(40.76, -73.984), new LatLng(40.67594, -73.98276)), 179.36, 3);

    }
}
