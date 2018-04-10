package com.Dytila.gauravpc.dytilasp1.LocationTracking;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.Dytila.gauravpc.dytilasp1.Kitchens;
import com.Dytila.gauravpc.dytilasp1.MainActivity;
import com.Dytila.gauravpc.dytilasp1.R;
import com.Dytila.gauravpc.dytilasp1.askDytilaMain;
import com.Dytila.gauravpc.dytilasp1.features;
import com.Dytila.gauravpc.dytilasp1.welcome_screens;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * Created by Gaurav Pc on 4/10/2018.
 */

public class CustomLocationListener {

    private static final String TAG = CustomLocationListener.class.getSimpleName();
    private Context context;
    private SpeedTimeMapper speedTimeMapper;
    private Kitchens kitchens;
    Handler handler;
    private long delay = 5000;
    Runnable runnable;
    private LatLng latLng;
    private int id = 1;

    public CustomLocationListener(Context context, SpeedTimeMapper speedTimeMapper){
        this.context = context;
        this.speedTimeMapper = speedTimeMapper;
        kitchens = new Kitchens();
        handler = new Handler();
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            latLng = kitchens.getLocation();
            sendDataToServer(latLng);
        }
    };

    public void getLocation(){
       handler.postDelayed(mRunnable,delay);
    }

    public void stopHandler(){
        handler.removeCallbacks(mRunnable);
    }

    private void sendDataToServer(final LatLng latLng){
        String url = "https://dytila.herokuapp.com/location";
        RequestQueue requestQueue=new Volley().newRequestQueue(context);
        final StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        delay = speedTimeMapper.time;
                        getLocation();
                        Log.e(TAG,"data sent");

                        sendNotification();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopHandler();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("lat", String.valueOf(latLng.latitude));
                params.put("lang",String.valueOf(latLng.longitude));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void sendNotification(){

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle("Dytila Kitchen found")
                .setContentText("You are nearby a dytila  kitchen")
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id /* ID of notification */,
                notificationBuilder.build());
    }
}
