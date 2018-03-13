package com.Dytila.gauravpc.dytilasp1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

public class How_to_Order_Meal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to__order__meal);
        // to load img from server and save it to chache
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false).cacheOnDisk(false).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);


        ImageView img1,img2,img3,img4,img5;
        img1=(ImageView)findViewById(R.id.one);
        img2=(ImageView)findViewById(R.id.two);
        img3=(ImageView)findViewById(R.id.three);
        img4=(ImageView)findViewById(R.id.four);
        img5=(ImageView)findViewById(R.id.five);

        for(int i=2;i<=6;i++){
            MemoryCacheUtils.removeFromCache("https://dytila.herokuapp.com/static/img/app%20pics/hto"+i+".png", ImageLoader.getInstance().getMemoryCache());
            DiskCacheUtils.removeFromCache("https://dytila.herokuapp.com/static/img/app%20pics/hto"+i+".png", ImageLoader.getInstance().getDiskCache());
        }

        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/hto2.png", img1);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/hto3.png", img2);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/hto4.png", img3);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/hto5.png", img4);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/hto6.png", img5);

    }

}
