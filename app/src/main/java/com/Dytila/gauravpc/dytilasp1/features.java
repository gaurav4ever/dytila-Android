package com.Dytila.gauravpc.dytilasp1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Dytila.gauravpc.dytilasp1.mealsHandler.MapModel;
import com.Dytila.gauravpc.dytilasp1.models.MyLocListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class features extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    TextView user_name,user_email,welcomeText1;
    RelativeLayout rl0,rl1_1,rl1,rl2,rl3,rl4,rl5,rl6,rl7,rl8,rl9;
    ImageView userAvatar;
    String avatar_url;
    String userIDValue;
    private String appId="ca-app-pub-6982833997588904~1633296873";
    private String unitId="ca-app-pub-6982833997588904/9016962876";

    //variable to find nearest dytila kitchen
    private ProgressDialog pDialog;
    double myLat, myLang;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    MyLocListener myLocListener;
    List<MapModel> mapModelList=new ArrayList<>();

//    Menu Icons Images
    ImageView vegMealIcon,nonVegMealIcon,eggMealIcon,saladsIcon,juciesIcon,shakesIcon,cheatIcon,customIcon;
//    Bottom Extra Images
    ImageView kitchenIcon,faqsIcon,feedbackIcon,facebookIcon,instaIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String username_val=sharedPreferences.getString("username", "");
        final String userid_val=sharedPreferences.getString("user_id", "");
        String mobile_val=sharedPreferences.getString("mobile","");
        String email_val=sharedPreferences.getString("email","");
        String avatar_val=sharedPreferences.getString("avatar","");avatar_url=avatar_val;
        String order_id=sharedPreferences.getString("order_id","");
        final String TmealFreq_val=sharedPreferences.getString("TmealFreq","");
        String mealFreq_val=sharedPreferences.getString("mealFreq","");
        final String mKitchens=sharedPreferences.getString("mKitchen", "");
        final String isFirstTime=sharedPreferences.getString("firstTime", "");
        editor.commit();
        //Shared Preferences Ends
        userIDValue=userid_val; //for menu item

        // to load img from server and save it to chache
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        //Navigation Operation Begins
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,account.class);
                startActivity(intent);
            }
        });
        user_name=(TextView)findViewById(R.id.username);
        user_email=(TextView)findViewById(R.id.user_email);
        user_name.setText(username_val);
        user_email.setText(email_val);
        userAvatar=(ImageView)findViewById(R.id.userImg);
        ImageLoader.getInstance().displayImage(avatar_val, userAvatar);

        RelativeLayout businessLayout=(RelativeLayout)findViewById(R.id.businessLayout);
        businessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,dytilab.class);
                startActivity(intent);
            }
        });

        LinearLayout homeLayout,accountLayout,myOrdersLayout,favMealsLayout,billsLayout,renewLayout,logoutLayout,aboutUsLayout;
        homeLayout=(LinearLayout)findViewById(R.id.home);
        accountLayout=(LinearLayout)findViewById(R.id.account);
        myOrdersLayout=(LinearLayout)findViewById(R.id.my_orders);
        favMealsLayout=(LinearLayout)findViewById(R.id.fav_meals);
        billsLayout=(LinearLayout)findViewById(R.id.bills);
        renewLayout=(LinearLayout)findViewById(R.id.renew);
        logoutLayout=(LinearLayout)findViewById(R.id.logout);
        aboutUsLayout=(LinearLayout)findViewById(R.id.aboutus);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,account.class);
                startActivity(intent);
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,Order_details.class);
                startActivity(intent);
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(features.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,bills.class);
                startActivity(intent);
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,renew.class);
                startActivity(intent);
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("userInfo",Context.MODE_APPEND);
                sharedPreferences.edit().clear().commit();
                String loginMsg="0";
                try {
                    FileOutputStream fileOutputStream=openFileOutput("Dytila Login",MODE_APPEND);
                    fileOutputStream.write(loginMsg.getBytes());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(features.this,activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

//        MobileAds.initialize(getApplicationContext(),"ca-app-pub-6982833997588904~1633296873");
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        welcomeText1=(TextView)findViewById(R.id.welcomeText);
        welcomeText1.setText(upperFirst("Welcome " + username_val));

        if( isFirstTime.equals("0")){
            boolean check=isNetworkAvailable();
            if(check==true) {
                String url="https://dytila.herokuapp.com/api/accounts/"+userid_val;
                Log.e("values",url);
                new JSONTask().execute(url);
            }
            else{
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                user_email.setText(email_val);
            }
        }


//        Food varietes icons
        vegMealIcon=(ImageView)findViewById(R.id.veg_icon);
        nonVegMealIcon=(ImageView)findViewById(R.id.nonveg_icon);
        eggMealIcon=(ImageView)findViewById(R.id.egg_icon);
        saladsIcon=(ImageView)findViewById(R.id.salad_icon);
        juciesIcon=(ImageView)findViewById(R.id.juices_icon);
        shakesIcon=(ImageView)findViewById(R.id.shakes_icon);
        customIcon=(ImageView)findViewById(R.id.custom_icon);
        cheatIcon=(ImageView)findViewById(R.id.cheat_icon);

        vegMealIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,vegmeals.class);
                startActivity(intent);
            }
        });
        nonVegMealIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,nonvegmeals.class);
                startActivity(intent);
            }
        });
        eggMealIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,egg.class);
                startActivity(intent);
            }
        });
        saladsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        juciesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(features.this, Juices.class);
                startActivity(intent);
            }
        });
        shakesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,shakes.class);
                startActivity(intent);
            }
        });
        cheatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,cheat.class);
                startActivity(intent);
            }
        });
        customIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,custom_meal_plans.class);
                startActivity(intent);
            }
        });

//        Bottom extra icons


        //Horizontl scroll view features operation

        rl1=(RelativeLayout)findViewById(R.id.il1);//askDytila
        rl2=(RelativeLayout)findViewById(R.id.il2);//slabs
        rl3=(RelativeLayout)findViewById(R.id.il3);//calculators
        rl7=(RelativeLayout)findViewById(R.id.il7);//mKitchen
        rl8=(RelativeLayout)findViewById(R.id.il8);

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(features.this, askDytilaMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(features.this, slabs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(features.this,calculator.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        rl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mKitchens.equals("unavailable")){
                    final Dialog dialog = new Dialog(features.this);
                    dialog.setContentView(R.layout.can_order_layout);
                    TextView close = (TextView) dialog.findViewById(R.id.close);
                    close.setText("Find Again");
                    dialog.show();
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            findDytilaKitchen();
                        }
                    });
                }
                else if(mKitchens.equals("null")){
                    findDytilaKitchen();
                }else{

                    boolean check=isNetworkAvailable();
                    if(check==true) {
                        String[] val=mKitchens.split(":");

                        String add=val[2];
                        String mobile=val[3];

                        String full_address_of_kitchen="Address:\n"+add;

                        final Dialog dialog = new Dialog(features.this);
                        dialog.setContentView(R.layout.cannot_order_layout);

                        TextView textView=(TextView) dialog.findViewById(R.id.t2);
                        textView.setText("Dytila Kitchen near you");

                        TextView close = (TextView) dialog.findViewById(R.id.close);
                        close.setText("Find Again");
                        dialog.show();
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                findDytilaKitchen();
                            }
                        });

                        TextView addressText=(TextView) dialog.findViewById(R.id.address);
                        addressText.setText(full_address_of_kitchen);

                        TextView mobileText=(TextView)dialog.findViewById(R.id.mobile);
                        mobileText.setText(mobile);
                        dialog.show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        rl8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(features.this);
                View parentView=getLayoutInflater().inflate(R.layout.bottomsheet_features, null);

//                kitchenIcon=(ImageView)parentView.findViewById(R.id.kitchen_icon);
                faqsIcon=(ImageView)parentView.findViewById(R.id.kitchens_icon);
                feedbackIcon=(ImageView)parentView.findViewById(R.id.feedback_icon);
                facebookIcon=(ImageView)parentView.findViewById(R.id.facebook_icon);
                instaIcon=(ImageView)parentView.findViewById(R.id.instagram_icon);

//                kitchenIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (ActivityCompat.checkSelfPermission(features.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(features.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                                //This is called if user has denied the permission before
//                                //In this case I am just asking the permission again
//                                ActivityCompat.requestPermissions(features.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//                            }
//                            else {
//                                ActivityCompat.requestPermissions(features.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                                Log.e("yes", "yes");
//                            }
//
//                        }
//                        else{
//                            int off = 0;
//                            try {
//                                off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
//                            } catch (Settings.SettingNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                            if (off == 0) {
//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(features.this);
//                                alertDialogBuilder.setMessage("Oops! GPS is OFF... would you like to enable it ?");
//                                alertDialogBuilder.setPositiveButton("Yes! Take Me There",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface arg0, int arg1) {
//                                                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                                startActivity(onGPS);
//                                            }
//                                        });
//
//                                AlertDialog alertDialog = alertDialogBuilder.create();
//                                alertDialog.show();
//                            }
//                            else{
//                                Toast.makeText(getApplicationContext(),"Loading Maps Please Wait",Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(features.this, places_kitchens.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.putExtra("type", "Kitchens");
//                                startActivity(intent);
//                            }
//                        }
//                    }
//                });
                faqsIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(features.this,Kitchens.class);
                        intent.putExtra("value", "Kitchens");
                        startActivity(intent);
                    }
                });
                feedbackIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(features.this);
                        dialog.setContentView(R.layout.feedback_layout);
                        final EditText editText=(EditText)dialog.findViewById(R.id.feedback_text);
                        TextView textView=(TextView)dialog.findViewById(R.id.send_feedback_button);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                giveFeedback(userid_val, editText.getText().toString());
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                facebookIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/167551100331762"));
                        startActivity(intent);
                    }
                });
                instaIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dytila_nutrition"));
                        startActivity(intent);
                    }
                });
                bottomSheetDialog.setContentView(parentView);
                bottomSheetDialog.show();
            }
        });
        //horizontal scroll view options end



        //dytila features image operations
        ImageView image1,image2,image3,image4,image5,image6,image7,image8,image9;

        image1=(ImageView)findViewById(R.id.img0);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/o1.png", image1);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(features.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.offer_layout);
                TextView close = (TextView) dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        image2=(ImageView)findViewById(R.id.img1);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/o3.png", image2);
        image3=(ImageView)findViewById(R.id.img2);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/o4.png", image3);
        image4=(ImageView)findViewById(R.id.img3);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/o5.png", image4);
        image5=(ImageView)findViewById(R.id.img4);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/o10.png", image5);
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(features.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_gluten_free);
                TextView ans1,ans2,ans3,ans4;
                ans1=(TextView)dialog.findViewById(R.id.ans1);
                ans2=(TextView)dialog.findViewById(R.id.ans2);
                ans3=(TextView)dialog.findViewById(R.id.ans3);
                ans4=(TextView)dialog.findViewById(R.id.ans4);
                ans1.setText("Gluten is a general name for the proteins found in wheat " +
                        "(wheatberries, durum, emmer, semolina, spelt, farina, farro, graham, KAMUT® khorasan wheat and einkorn)" +
                        ", rye, barley and triticale – a cross between wheat and rye." +
                        " Gluten helps foods maintain their shape, acting as a glue that holds food together." +
                        " Gluten can be found in many types of foods, even ones that would not be expected.\n");
                ans2.setText("Gluten causes health problems for those with gluten-related disorders, including celiac disease (CD), non-celiac gluten sensitivity (NCGS), gluten ataxia, dermatitis herpetiformis (DH) and wheat allergy. In these patients, the gluten-free diet is demonstrated as an effective treatment. In addition, at least in some cases, the gluten-free diet may improve gastrointestinal and/or systemic symptoms in diseases like irritable bowel syndrome, rheumatoid arthritis, multiple sclerosis or HIV enteropathy, among others.");
                ans3.setText("A gluten-free diet is a diet that excludes gluten, a protein composite found in wheat, barley, rye, and all their species and hybrids");
                ans4.setText("Dytila provides some meals which are Gluten Free!");
                TextView close = (TextView) dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        image6=(ImageView)findViewById(R.id.img5);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/O7.png", image6);
        image7=(ImageView)findViewById(R.id.img6);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/o6.png", image7);
        image8=(ImageView)findViewById(R.id.img7);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/o8.png", image8);
        image9=(ImageView)findViewById(R.id.img8);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/o2.png", image9);
        image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(features.this, feedback.class);
                intent.putExtra("value", "contactus");
                startActivity(intent);
            }
        });


        //find the closest dytila kitchen from user
        String mDytilaKitchenCoordinates=sharedPreferences.getString("mKitchen", "");
        if(mDytilaKitchenCoordinates.equals("null")){
            findDytilaKitchen();
        }
        if(mKitchens.equals("null") || mKitchens.equals("unavailable")){
            //change the heart image to broken heart
            ImageView imageView=(ImageView)findViewById(R.id.myKitchen);
            imageView.setImageResource(R.drawable.bheart);
        }else{
            ImageView imageView=(ImageView)findViewById(R.id.myKitchen);
            imageView.setImageResource(R.drawable.heart);
        }

        RelativeLayout howToOrderLayout=(RelativeLayout)findViewById(R.id.howToOrderLayout);
        howToOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(features.this,How_to_Order_Meal.class);
                startActivity(intent);
            }
        });
    }


    //functions to find nearest dytila Kitchens
    public void findDytilaKitchen(){
        if (ActivityCompat.checkSelfPermission(features.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(features.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //user did not gave permission or did not gave any response
                //Ask again for permission
                ActivityCompat.requestPermissions(features.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //Ask for permission first time
                ActivityCompat.requestPermissions(features.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

        } else {
            int off = 0;
            try {
                off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            if (off == 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(features.this);
                alertDialogBuilder.setMessage("We are unable to find any Dytila Kitchen near you because GPS is OFF... would you like to enable it ?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(onGPS);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {

                myLocListener = new MyLocListener(features.this);
                if (myLocListener.canGetLocation()) {
                    myLat = myLocListener.getLat();
                    myLang = myLocListener.getLang();
                }

                findKitchens();
            }
        }
    }
    public void findKitchens(){

        final double minD[]={5000};
        final double min_lat[] = new double[1],min_lang[]= new double[1];
        final String[] kitchen_id = new String[1];
        final String[] add = new String[1];
        final String[] mobile = new String[1];
        String url="https://dytila.herokuapp.com/api/places_kitchens";
        pDialog = new ProgressDialog(features.this);
        pDialog.setMessage("Finding Nearest Dytila Kitchen...");
        pDialog.setCancelable(false);

        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray parentArray=response.getJSONArray("kitchens");

                    double dk_lat,dk_lang;

                    for(int i=0;i<parentArray.length();i++){
                        JSONObject finalObject=parentArray.getJSONObject(i);
                        MapModel mapModel=new MapModel();
                        mapModel.setId(finalObject.getString("kitchen_id"));
                        mapModel.setLat(finalObject.getString("latitude"));
                        mapModel.setLang(finalObject.getString("longitude"));
                        mapModel.setAdd(finalObject.getString("address"));
                        mapModel.setMobile(finalObject.getString("mobile"));

                        dk_lat=Double.parseDouble(finalObject.getString("latitude"));
                        dk_lang=Double.parseDouble(finalObject.getString("longitude"));
                        int r = 6371;
                        double latD = Math.toRadians(dk_lat - myLat);
                        double longD = Math.toRadians(dk_lang - myLang);
                        double a = Math.sin(latD / 2) * Math.sin(latD / 2) + Math.cos(Math.toRadians(dk_lat)) * Math.cos(Math.toRadians(myLat))
                                * Math.sin(longD / 2) * Math.sin(longD / 2);
                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                        double distance = r * c * 1000;//convert to meters
                        if (distance < minD[0]) {
                            kitchen_id[0]=finalObject.getString("kitchen_id");
                            minD[0]=distance;
                            min_lat[0]=dk_lat;
                            min_lang[0]=dk_lang;
                            add[0] =finalObject.getString("address");
                            mobile[0]=finalObject.getString("mobile");
                        }

                        mapModelList.add(mapModel);
                    }
                    pDialog.dismiss();
                    postExecute(kitchen_id[0],minD[0],min_lat[0],min_lang[0],add[0],mobile[0]);
                    //check if user distance is less than 500m from any dytila kitchen
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Some Thing Went Wrong! Please Try Again!", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Some Thing Went Wrong! Please Try Again!",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void postExecute(String kitchen_id,double minD,double lat,double lang,String add,String mobile){

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        if(minD==5000){

            //change the heart image to broken heart
            ImageView imageView=(ImageView)findViewById(R.id.myKitchen);
            imageView.setImageResource(R.drawable.bheart);

            editor.putString("mKitchen", "unavailable");
            final Dialog dialog = new Dialog(features.this);
            dialog.setContentView(R.layout.can_order_layout);
            TextView close = (TextView) dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        else{

            //change the broken heart image to heart
            ImageView imageView=(ImageView)findViewById(R.id.myKitchen);
            imageView.setImageResource(R.drawable.heart);

            editor.putString("mKitchen", lat+":"+lang+":"+add+":"+mobile+":"+kitchen_id);

            String full_address_of_kitchen="Address:\n"+add;

            final Dialog dialog = new Dialog(features.this);
            dialog.setContentView(R.layout.cannot_order_layout);

            TextView close = (TextView) dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            TextView addressText=(TextView) dialog.findViewById(R.id.address);
            addressText.setText(full_address_of_kitchen);

            TextView mobileText=(TextView)dialog.findViewById(R.id.mobile);
            mobileText.setText(mobile);
            dialog.show();
        }
        editor.commit();
    }
    //End functions



    private String ordinal_suffix_of(int i) {
        int j = i % 10,
                k = i % 100;
        if (j == 1 && k != 11) {
            return i + "st";
        }
        if (j == 2 && k != 12) {
            return i + "nd";
        }
        if (j == 3 && k != 13) {
            return i + "rd";
        }
        return i + "th";
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public class JSONTask extends AsyncTask<String,String ,String> {

        private ProgressDialog dialog = new ProgressDialog(features.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setTitle("Refreshing ...");
            this.dialog.setMessage("Please Wait ...");
            this.dialog.setCancelable(false);
            this.dialog.show();

            //timeout for Async Task
            final JSONTask asyncObject = this;
            new CountDownTimer(10000, 10000) {
                public void onTick(long millisUntilFinished) {
                    // monitor the progress
                }
                public void onFinish() {
                    // stop async task if not in progress
                    if (asyncObject.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncObject.cancel(false);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(features.this);
                        alertDialogBuilder.setMessage("Connection Is Unstable!");
                        alertDialogBuilder.setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(features.this, features.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(features.this, features.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }.start();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson=buffer.toString();
                JSONObject parentObject=new JSONObject(finalJson);

                String name_val=parentObject.getString("name");
                String email_val=parentObject.getString("email");
                String mobile_val=parentObject.getString("mobile");
                String user_id_val=parentObject.getString("id");
                String avatar_val=parentObject.getString("avatar");
                String order_id_val=parentObject.getString("order_id");
                String food_name_val=parentObject.getString("food_name");
                String Tmeal_freq_val=parentObject.getString("total meal freq");
                String meal_freq_val=parentObject.getString("meal freq");
                String location_val=parentObject.getString("location");
                String time_val=parentObject.getString("time");
                String spec_val=parentObject.getString("spec");

                String values=name_val+       //0
                        "-"+email_val         //1
                        +"-"+mobile_val       //2
                        +"-"+user_id_val      //3
                        +"-"+order_id_val     //4
                        + "-"+food_name_val   //5
                        +"-"+Tmeal_freq_val   //6
                        +"-"+meal_freq_val    //7
                        +"-"+location_val     //8
                        +"-"+time_val         //9
                        +"-"+spec_val         //10
                        +"-"+avatar_val;      //11
                return values;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String[] values=result.split("-");


            SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            String isFirstTime=sharedPreferences.getString("firstTime","");

            editor.putString("order_id",values[4]);
            editor.putString("mealName",values[5]);
            editor.putString("TmealFreq",values[6]);
            editor.putString("mealFreq",values[7]);
            if(values[8].equals("null"))
                editor.putString("location","Not Specified");
            else
                editor.putString("location",values[8]);
            editor.putString("timing",values[9]);
            editor.putString("specs", values[10]);
            editor.putString("avatar", values[11]);
            editor.commit();

            sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
            editor = sharedPreferences.edit();
            editor.putString("firstTime","1");
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("firstTime","0");
            editor.commit();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("menu option", "Menu Made");
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

//    onNavigationItemSelected

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("menu option", "Menu item now");
        int id = item.getItemId();

        if(id==R.id.action_refresh){
            boolean check=isNetworkAvailable();
            if(check==true) {
                SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("firstTime", "0");
                editor.commit();
                MemoryCacheUtils.removeFromCache(avatar_url, ImageLoader.getInstance().getMemoryCache());
                DiskCacheUtils.removeFromCache(avatar_url, ImageLoader.getInstance().getDiskCache());
                Intent intent=new Intent(features.this,features.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void giveFeedback(final String user_id,final String feedback){
        String url="https://dytila.herokuapp.com/api/feedback";
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(features.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        final Dialog dialog = new Dialog(features.this);
                        dialog.setContentView(R.layout.layout_thankyou);
                        TextView close = (TextView) dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(features.this);
                alertDialogBuilder.setMessage("Connection Is Unstable :( Please Try Again!");
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("user_id",user_id);
                params.put("feedback",feedback);
                return params;
            }
        };
        int socketTimeout = 5000;//30 seconds - for image loading to heroku -> dropbox -> heroku and getting response back to app
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
    public String upperFirst(String val){
        String final_answer="";
        for(int i=0;i<val.length();i++){
            if(i==0)
                final_answer+=(String.valueOf(val.charAt(i))).toUpperCase();
            else if(String.valueOf(val.charAt(i-1)).equals(" "))
                final_answer+=(String.valueOf(val.charAt(i))).toUpperCase();
            else if(!String.valueOf(val.charAt(i)).equals(" "))
                final_answer+=(String.valueOf(val.charAt(i))).toLowerCase();
            else
                final_answer+=String.valueOf(val.charAt(i));
        }
        return final_answer;
    }
    
}