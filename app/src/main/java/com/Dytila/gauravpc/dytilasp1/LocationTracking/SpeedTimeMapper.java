package com.Dytila.gauravpc.dytilasp1.LocationTracking;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.Dytila.gauravpc.dytilasp1.models.MyLocListener;

/**
 * Created by Gaurav Pc on 4/8/2018.
 */

public class SpeedTimeMapper {

    private GPSCallback gpsCallback;
    private MyLocListener myLocListener;
    private Context context;
    public long time = 3000;

    public SpeedTimeMapper(Context context, MyLocListener myLocListener){
        this.context = context;
        this.myLocListener = myLocListener;
    }

    public void findNextUpdateTime(final double speed){
        time = mapper(speed);
        Log.e("time",""+time);
        myLocListener.updateTime(time);
    }

    private long mapper(double speed){
        if(speed >=0 && speed<10){
            return 5000;
        } else if(speed>=10 && speed<20){
            return 3000;
        } else if(speed>=20 && speed<40){
            return 2000;
        }
        return 1000;
    }

    public void setGPSCallback(final GPSCallback gpsCallback) {
        this.gpsCallback = gpsCallback;
    }
}
