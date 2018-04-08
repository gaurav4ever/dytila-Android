package com.Dytila.gauravpc.dytilasp1.LocationTracking;

import android.location.Location;

/**
 * Created by Gaurav Pc on 4/8/2018.
 */

public interface GPSCallback {

    public abstract void onGPSUpdate(Location location);
}
