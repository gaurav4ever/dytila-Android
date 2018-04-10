package com.Dytila.gauravpc.dytilasp1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.Dytila.gauravpc.dytilasp1.LocationTracking.CustomLocationListener;
import com.Dytila.gauravpc.dytilasp1.LocationTracking.GPSCallback;
import com.Dytila.gauravpc.dytilasp1.LocationTracking.SpeedTimeMapper;
import com.Dytila.gauravpc.dytilasp1.models.MyLocListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.BigDecimal;

public class Kitchens extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GPSCallback {

    private MyLocListener myLocListener;
    private double myLat;
    private double myLang;
    private GoogleMap mMap;
    private SpeedTimeMapper speedTimeMapper;

    private boolean isGPSEnabled;
    float speed;
    double currentSpeed, kmphSpeed;
    private TextView textView;
    private MarkerOptions markerOptions;
    private Marker myMarker;

    private double latFinal, langFinal;
    private CustomLocationListener customLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchens);
        initViews();

        textView = (TextView) findViewById(R.id.textView);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void initViews() {
        markerOptions = new MarkerOptions().position(new LatLng(0, 0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        myLocListener = new MyLocListener(Kitchens.this);
        myLocListener.setGPSCallback(this);
        if (myLocListener.canGetLocation()) {
            myLat = myLocListener.getLat();
            myLang = myLocListener.getLang();
        }
        speedTimeMapper = new SpeedTimeMapper(Kitchens.this, myLocListener);
        speedTimeMapper.setGPSCallback(this);

        customLocationListener = new CustomLocationListener(Kitchens.this, speedTimeMapper);
        customLocationListener.getLocation();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myMarker = mMap.addMarker(markerOptions);
        updateLocationOnMap(myLat, myLang);
    }

    @Override
    public void onGPSUpdate(Location location) {
        //update location on map
        updateLocationOnMap(location.getLatitude(), location.getLongitude());

        speed = location.getSpeed();
        latFinal = location.getLatitude();
        langFinal = location.getLongitude();

        currentSpeed = round(speed, 3, BigDecimal.ROUND_HALF_UP);
        kmphSpeed = round((currentSpeed * 3.6), 3, BigDecimal.ROUND_HALF_UP);

        textView.setText("Lat: " + location.getLatitude() + "\n Long: " + location.getLongitude() + "\nSpeed: " + kmphSpeed + "kmph");

        speedTimeMapper.findNextUpdateTime(kmphSpeed);
    }

    private void updateLocationOnMap(double lat, double lang) {
        LatLng p = new LatLng(lat, lang);
        myMarker.setPosition(p);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p, 16));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(false);
    }

    public static double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

    public LatLng getLocation() {
        return new LatLng(latFinal, langFinal);
    }

    @Override
    public void onBackPressed() {
        customLocationListener.stopHandler();
        super.onBackPressed();
    }
}


