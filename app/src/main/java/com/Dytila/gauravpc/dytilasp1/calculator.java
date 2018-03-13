package com.Dytila.gauravpc.dytilasp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class calculator extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    TextView user_name,user_email;
    RelativeLayout c1,c2,c3;
    ImageView userAvatar,goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        String userid_val=sharedPreferences.getString("id","");
        String mobile_val=sharedPreferences.getString("mobile","");
        String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String avatar_val=sharedPreferences.getString("avatar","");
        //Shared Preferences Ends

        //Navigation Operation Begins

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(calculator.this,account.class);
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
                Intent intent=new Intent(calculator.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(calculator.this,account.class);
                startActivity(intent);
                finish();
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(calculator.this,Order_details.class);
                startActivity(intent);
                finish();
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calculator.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(calculator.this,bills.class);
                startActivity(intent);
                finish();
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(calculator.this,renew.class);
                startActivity(intent);
                finish();
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
                Intent intent=new Intent(calculator.this,activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(calculator.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

        //calculators
        c1=(RelativeLayout)findViewById(R.id.cal1);
        c2=(RelativeLayout)findViewById(R.id.cal2);
        c3=(RelativeLayout)findViewById(R.id.cal3);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"This calculator is coming soon!",Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(calculator.this,showCalculator.class);
//                intent.putExtra("calType","bodyFatCal");
//                startActivity(intent);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(calculator.this,showCalculator.class);
                intent.putExtra("calType","calorieCal");
                startActivity(intent);
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(calculator.this,showCalculator.class);
                intent.putExtra("calType","bmiCal");
                startActivity(intent);
            }
        });

        goBack=(ImageView)findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent intent=new Intent(calculator.this,features.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

}
