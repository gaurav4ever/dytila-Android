package com.Dytila.gauravpc.dytilasp1;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class order_now extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    String userIDValue;
    TextView user_name,user_email;
    ImageView userAvatar;
    TextView headText,tmeal,lmeal;
    Button callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String userid_val=sharedPreferences.getString("user_id", "");
        String username_val=sharedPreferences.getString("username", "");
        String mobile_val=sharedPreferences.getString("mobile", "");
        String email_val=sharedPreferences.getString("email", "");
        String orderid_val=sharedPreferences.getString("order_id", "");
        String mealName_val=sharedPreferences.getString("mealName", "");
        String location_val=sharedPreferences.getString("location","");
        String TmealFreq_val=sharedPreferences.getString("TmealFreq","");
        String mealFreq_val=sharedPreferences.getString("mealFreq","");
        String timing_val=sharedPreferences.getString("timing","");
        String specs_val=sharedPreferences.getString("specs","");
        String avatar_val=sharedPreferences.getString("avatar","");
        final String mKitchen=sharedPreferences.getString("mKitchen","");
        //Shared Preferences Ends


        //Navigation Operation Begins

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(order_now.this,account.class);
                startActivity(intent);
            }
        });
        user_name=(TextView)findViewById(R.id.username);
        user_email=(TextView)findViewById(R.id.user_email);
        user_name.setText(username_val);
        user_email.setText(email_val);
        userAvatar=(ImageView)findViewById(R.id.userImg);
        ImageLoader.getInstance().displayImage(avatar_val, userAvatar);
        LinearLayout homeLayout,accountLayout,myOrdersLayout,billsLayout,renewLayout,logoutLayout,aboutUsLayout,favMealsLayout;
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
                Intent intent=new Intent(order_now.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(order_now.this,account.class);
                startActivity(intent);
                finish();
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(order_now.this, Order_details.class);
                startActivity(intent);
                finish();
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(order_now.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(order_now.this, bills.class);
                startActivity(intent);
                finish();
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(order_now.this,renew.class);
                startActivity(intent);
                finish();
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
                sharedPreferences.edit().clear().commit();
                String loginMsg = "0";
                try {
                    FileOutputStream fileOutputStream = openFileOutput("Dytila Login", MODE_APPEND);
                    fileOutputStream.write(loginMsg.getBytes());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(order_now.this, activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(order_now.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

        Intent intent=getIntent();
        String current_meal_val=intent.getStringExtra("current_meal");
        headText=(TextView)findViewById(R.id.head);
        headText.setText("Your "+current_meal_val+" meal is waiting for you!");
        tmeal=(TextView)findViewById(R.id.Tmeal_freq);
        lmeal=(TextView)findViewById(R.id.meal_freq);

        tmeal.setText(TmealFreq_val);
        lmeal.setText(mealFreq_val);

        callButton=(Button)findViewById(R.id.call);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mKitchen.equals("null")){
                    final Dialog dialog = new Dialog(order_now.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.can_order_layout);
                    TextView t1Text=(TextView)dialog.findViewById(R.id.t1);
                    t1Text.setText("Oops! o_O");
                    TextView t2Text=(TextView)dialog.findViewById(R.id.t2);
                    t2Text.setText("Please let us find a kitchen near you!");
                    TextView t3Text=(TextView)dialog.findViewById(R.id.t3);
                    t3Text.setText("Go to home and click on 'My Dytila Kitchen' and then find again :)");
                    TextView close = (TextView) dialog.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(mKitchen.equals("unavailable")){
                    final Dialog dialog = new Dialog(order_now.this);
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
                else {
                    String mobileNumber="tel:"+mKitchen.split(":")[3];
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(mobileNumber));
                    if (ActivityCompat.checkSelfPermission(order_now.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(order_now.this, Manifest.permission.CALL_PHONE)) {
                            //This is called if user has denied the permission before
                            //In this case I am just asking the permission again
                            ActivityCompat.requestPermissions(order_now.this, new String[]{Manifest.permission.CALL_PHONE}, 0x2);

                        } else {
                            ActivityCompat.requestPermissions(order_now.this, new String[]{Manifest.permission.CALL_PHONE}, 0x2);
                        }
                    } else {
                        startActivity(callIntent);
                    }
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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


        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}

