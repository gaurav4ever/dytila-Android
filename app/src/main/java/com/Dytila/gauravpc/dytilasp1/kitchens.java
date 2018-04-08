package com.Dytila.gauravpc.dytilasp1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.Dytila.gauravpc.dytilasp1.models.MyLocListener;

public class kitchens extends AppCompatActivity {

    private MyLocListener myLocListener;
    private double myLat;
    private double myLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchens);
        initViews();
    }

    private void initViews() {
        myLocListener = new MyLocListener(kitchens.this);
        if (myLocListener.canGetLocation()) {
            myLat = myLocListener.getLat();
            myLang = myLocListener.getLang();
        }
    }

}
