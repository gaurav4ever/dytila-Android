package com.Dytila.gauravpc.dytilasp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

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

public class dytilab extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    TextView user_name,user_email;
    EditText email,mobile;
    Button submit;
    ImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dytilab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        String userid_val=sharedPreferences.getString("user_id", "");
        final String mobile_val=sharedPreferences.getString("mobile","");
        final String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String avatar_val=sharedPreferences.getString("avatar","");
        Log.e("values", userid_val);
        //Shared Preferences Ends

        //Navigation Operation Begins

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dytilab.this,account.class);
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
                Intent intent=new Intent(dytilab.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dytilab.this,account.class);
                startActivity(intent);
                finish();
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dytilab.this,Order_details.class);
                startActivity(intent);
                finish();
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dytilab.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dytilab.this,bills.class);
                startActivity(intent);
                finish();
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dytilab.this,renew.class);
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
                Intent intent=new Intent(dytilab.this,activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dytilab.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

        submit=(Button)findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=(EditText)findViewById(R.id.contact_email);
                mobile=(EditText)findViewById(R.id.contact_mobile);
                if(email.getText().toString().length()<1){
                    Toast.makeText(getApplicationContext(), "Please specify your Email ID", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(mobile.getText().toString().length()<1){
                        Toast.makeText(getApplicationContext(), "Please specify your Mobile Number", Toast.LENGTH_SHORT).show();
                    }else{
                        String values=email.getText().toString()+","+mobile.getText().toString();

                        boolean check=isNetworkAvailable();
                        if(check==true) {
                            new JSONTask().execute("https://dytila.herokuapp.com/api/dytilabusiness/" + values);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class JSONTask extends AsyncTask<String,String ,String> {

        private ProgressDialog dialog = new ProgressDialog(dytilab.this);
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.account_details);
        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Please Wait ...");
            this.dialog.show();



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
                String status_val=parentObject.getString("status");

                return status_val;

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

            if (result.equals("success")) {
                Toast.makeText(getApplicationContext(), "Successfully submitted! Thank you", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(dytilab.this,features.class);
                startActivity(intent);
            }

        }
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
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

}

