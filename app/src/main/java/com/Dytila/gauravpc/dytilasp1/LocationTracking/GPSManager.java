//package com.Dytila.gauravpc.dytilasp1.LocationTracking;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Criteria;
//import android.location.GpsSatellite;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.util.Log;
//
//import com.google.android.gms.location.LocationListener;
//
//import java.util.List;
//
///**
// * Created by Gaurav Pc on 4/8/2018.
// */
//
//public class GPSManager implements android.location.GpsStatus.Listener {
//    private static final int gpsMinTime = 500;
//    private static final int gpsMinDistance = 0;
//
//    private static LocationManager locationManager = null;
//    private static LocationListener locationListener = null;
//    private static GPSCallback gpsCallback = null;
//    Context mcontext;
//    public GPSManager(Context context) {
//        mcontext=context;
//        GPSManager.locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(final Location location) {
//                if (GPSManager.gpsCallback != null) {
//                    GPSManager.gpsCallback.onGPSUpdate(location);
//                }
//            }
//        };
//    }
//    public void showSettingsAlert(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
//        // Setting Dialog Title
//        alertDialog.setTitle("GPS is settings");
//        // Setting Dialog Message
//        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
//        // On pressing Settings button
//        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                mcontext.startActivity(intent);
//            }
//        });
//        // on pressing cancel button
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        // Showing Alert Message
//        alertDialog.show();
//    }
//    public GPSCallback getGPSCallback()
//    {
//        return GPSManager.gpsCallback;
//    }
//
//    public void setGPSCallback(final GPSCallback gpsCallback) {
//        GPSManager.gpsCallback = gpsCallback;
//    }
//
//    public void startListening(final Context context) {
//        if (GPSManager.locationManager == null) {
//            GPSManager.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        }
//
//        final Criteria criteria = new Criteria();
//
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setSpeedRequired(true);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//
//        final String bestProvider = GPSManager.locationManager.getBestProvider(criteria, true);
//
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//        }
//        locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER,
//                gpsMinTime,
//                gpsMinDistance, this);
//        }
//        else {
//            final List<String> providers = GPSManager.locationManager.getProviders(true);
//            for (final String provider : providers)
//            {
//                locationManager.requestLocationUpdates(provider, GPSManager.gpsMinTime,
//                        GPSManager.gpsMinDistance, GPSManager.locationListener);
//            }
//        }
//    }
//    public void stopListening() {
//        try
//        {
//            if (GPSManager.locationManager != null && GPSManager.locationListener != null) {
//                GPSManager.locationManager.removeUpdates((android.location.LocationListener) GPSManager.locationListener);
//            }
//            GPSManager.locationManager = null;
//        }
//        catch (final Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void onGpsStatusChanged(int event) {
//        int Satellites = 0;
//        int SatellitesInFix = 0;
//        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) mcontext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//        }
//        int timetofix = locationManager.getGpsStatus(null).getTimeToFirstFix();
//        Log.i("GPs", "Time to first fix = "+String.valueOf(timetofix));
//        for (GpsSatellite sat : locationManager.getGpsStatus(null).getSatellites()) {
//            if(sat.usedInFix()) {
//                SatellitesInFix++;
//            }
//            Satellites++;
//        }
//        Log.i("GPS", String.valueOf(Satellites) + " Used In Last Fix ("+SatellitesInFix+")");
//    }
//}
