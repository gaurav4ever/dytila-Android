package com.Dytila.gauravpc.dytilasp1.CustomMealsHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Dytila.gauravpc.dytilasp1.Order_details;
import com.Dytila.gauravpc.dytilasp1.R;
import com.Dytila.gauravpc.dytilasp1.about_us;
import com.Dytila.gauravpc.dytilasp1.account;
import com.Dytila.gauravpc.dytilasp1.activity1;
import com.Dytila.gauravpc.dytilasp1.askDytilaMain;
import com.Dytila.gauravpc.dytilasp1.bills;
import com.Dytila.gauravpc.dytilasp1.features;
import com.Dytila.gauravpc.dytilasp1.renew;
import com.Dytila.gauravpc.dytilasp1.slabs;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class customMealPlanDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView user_name,user_email;
    Toolbar toolbar;
    ImageView userAvatar;
    String user_id,username;
    private ProgressDialog pDialog;
    TextView mealPlanNameTextView,priceTextView,durationTextView,d1TextView,d2TextView,d3TextView,d4TextView,d5TextView,d6TextView,d7TextView;
    Button apply_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_meal_plan_detail);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        String userid_val=sharedPreferences.getString("user_id", "");
        String mobile_val=sharedPreferences.getString("mobile","");
        String avatar_val=sharedPreferences.getString("avatar","");
        String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String mealFreq_val=sharedPreferences.getString("mealFreq","");
        Log.e("values", userid_val);
        user_id=userid_val;username=username_val;
        //Shared Preferences Ends

        //Navigation Operation Begins

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(customMealPlanDetail.this,account.class);
                startActivity(intent);
            }
        });
        user_name=(TextView)findViewById(R.id.username);
        user_email=(TextView)findViewById(R.id.user_email);
        user_name.setText(username_val);
        user_email.setText(email_val);
        userAvatar=(ImageView)findViewById(R.id.userImg);
        ImageLoader.getInstance().displayImage(avatar_val, userAvatar);
        LinearLayout homeLayout,accountLayout,myOrdersLayout,billsLayout,renewLayout,logoutLayout,aboutUsLayout;
        homeLayout=(LinearLayout)findViewById(R.id.home);
        accountLayout=(LinearLayout)findViewById(R.id.account);
        myOrdersLayout=(LinearLayout)findViewById(R.id.my_orders);
        billsLayout=(LinearLayout)findViewById(R.id.bills);
        renewLayout=(LinearLayout)findViewById(R.id.renew);
        logoutLayout=(LinearLayout)findViewById(R.id.logout);
        aboutUsLayout=(LinearLayout)findViewById(R.id.aboutus);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(customMealPlanDetail.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(customMealPlanDetail.this,account.class);
                startActivity(intent);
                finish();
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(customMealPlanDetail.this, Order_details.class);
                startActivity(intent);
                finish();
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(customMealPlanDetail.this, bills.class);
                startActivity(intent);
                finish();
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(customMealPlanDetail.this,renew.class);
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
                Intent intent = new Intent(customMealPlanDetail.this, activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(customMealPlanDetail.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends


        Intent intent=getIntent();
        final String plan_id=intent.getStringExtra("plan_id");
        String mealPlanNameVal=intent.getStringExtra("mealPlanName");
        String priceVal=intent.getStringExtra("price");
        String durationVal=intent.getStringExtra("duration");
        String day1Val=intent.getStringExtra("day1");
        String day2Val=intent.getStringExtra("day2");
        String day3Val=intent.getStringExtra("day3");
        String day4Val=intent.getStringExtra("day4");
        String day5Val=intent.getStringExtra("day5");
        String day6Val=intent.getStringExtra("day6");
        String day7Val=intent.getStringExtra("day7");

        mealPlanNameTextView=(TextView)findViewById(R.id.meal_plan_name);
        priceTextView=(TextView)findViewById(R.id.price);
        durationTextView=(TextView)findViewById(R.id.duration);
        d1TextView=(TextView)findViewById(R.id.day1Meal);
        d2TextView=(TextView)findViewById(R.id.day2Meal);
        d3TextView=(TextView)findViewById(R.id.day3Meal);
        d4TextView=(TextView)findViewById(R.id.day4Meal);
        d5TextView=(TextView)findViewById(R.id.day5Meal);
        d6TextView=(TextView)findViewById(R.id.day6Meal);
        d7TextView=(TextView)findViewById(R.id.day7Meal);

        mealPlanNameTextView.setText(mealPlanNameVal);
        priceTextView.setText("Price : "+priceVal);
        durationTextView.setText("Duration : "+durationVal);
        d1TextView.setText(day1Val);
        d2TextView.setText(day2Val);
        d3TextView.setText(day3Val);
        d4TextView.setText(day4Val);
        d5TextView.setText(day5Val);
        d6TextView.setText(day6Val);
        d7TextView.setText(day7Val);

        apply_button=(Button)findViewById(R.id.applyButton);
        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = isNetworkAvailable();
                if (check == true) {
                    postData(plan_id,user_id);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void postData(final String plan_id, final String user_id){
        String url="https://dytila.herokuapp.com/custom_plans/apply";

        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(customMealPlanDetail.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Applied Successfully",Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong!",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("plan_id",plan_id);
                params.put("user_id",user_id);
                return params;
            }
        };
        requestQueue.add(stringRequest);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.home_id){
            Intent intent=new Intent(customMealPlanDetail.this,features.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.bill_id){
            Intent intent=new Intent(customMealPlanDetail.this,bills.class);
            startActivity(intent);
        }
        else if(id==R.id.account_id){
            Intent intent=new Intent(customMealPlanDetail.this,account.class);
            startActivity(intent);
        }
        else if(id==R.id.order_id){
            Intent intent=new Intent(customMealPlanDetail.this,Order_details.class);
            startActivity(intent);
        }
        else if(id==R.id.renew_id){
            Intent intent=new Intent(customMealPlanDetail.this,renew.class);
            startActivity(intent);
        }
        else if(id==R.id.logout_id){
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
            Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(customMealPlanDetail.this,activity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.askDytila_id) {
            Intent intent=new Intent(customMealPlanDetail.this,askDytilaMain.class);
            startActivity(intent);

        }else if (id == R.id.slabs_id) {
            Intent intent=new Intent(customMealPlanDetail.this,slabs.class);
            startActivity(intent);

        }else if (id == R.id.facebook_id) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/167551100331762"));
            startActivity(intent);

        } else if (id == R.id.instagram_id) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dytila_nutrition"));
            startActivity(intent);
        } else if (id == R.id.aboutus_id) {

            Intent intent=new Intent(customMealPlanDetail.this,about_us.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("menu option", "Menu Made");
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

}
