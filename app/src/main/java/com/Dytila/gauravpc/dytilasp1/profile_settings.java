package com.Dytila.gauravpc.dytilasp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class profile_settings extends AppCompatActivity {

    TextView currentEmail,currentMobile,currentaddress;
    EditText email,mobile,delAddress;
    Button saveEmailButton,saveMobileButton,goHome1,goHome2;
    RelativeLayout l1,l2,l3;
    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        final String userid_val=sharedPreferences.getString("user_id", "");
        String mobile_val=sharedPreferences.getString("mobile","");
        String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String location=sharedPreferences.getString("location","");
        Log.e("values", userid_val);
        //Shared Preferences Ends

        l1=(RelativeLayout)findViewById(R.id.changeEmailLayout);
        l2=(RelativeLayout)findViewById(R.id.changeMobileLayout);
        l3=(RelativeLayout)findViewById(R.id.changeAddressLayout);

        String data = getIntent().getExtras().getString("setting","");
        if(data.equals("Change Email")){
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);

            currentEmail=(TextView)findViewById(R.id.current_email);
            currentEmail.setText("Current Email : " + email_val);
            currentEmail.setTextSize(7 * getResources().getDisplayMetrics().density);

            saveEmailButton=(Button)findViewById(R.id.save_button1);
            saveEmailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = (EditText) findViewById(R.id.new_email);
                    String new_email = email.getText().toString();

                    boolean check = isNetworkAvailable();
                    if (check == true) {
                        String url = "https://dytila.herokuapp.com/api/changeEmail/" + userid_val + "," + new_email;
                        Log.e("values", url);
                        new JSONTask().execute(url);
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", new_email);
                        editor.commit();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            goBack=(ImageView)findViewById(R.id.goback1);
            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }
        else if(data.equals("Change Mobile")){
            l1.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);

            currentMobile=(TextView)findViewById(R.id.current_mobile);
            currentMobile.setText("Current Mobile Number : " + mobile_val);
            currentMobile.setTextSize(7 * getResources().getDisplayMetrics().density);

            saveMobileButton=(Button)findViewById(R.id.save_button2);
            saveMobileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobile = (EditText) findViewById(R.id.new_mobile);
                    String new_mobile = mobile.getText().toString();

                    boolean check = isNetworkAvailable();
                    if (check == true) {
                        String url = "https://dytila.herokuapp.com/api/changeMobile/" + userid_val + "," + new_mobile;
                        Log.e("values", url);
                        new JSONTask().execute(url);
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mobile", new_mobile);
                        editor.commit();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            goBack=(ImageView)findViewById(R.id.goback2);
            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        else if(data.equals("Change Address")){
            l1.setVisibility(View.GONE);
            l2.setVisibility(View.GONE);

            currentaddress=(TextView)findViewById(R.id.current_address);
            currentaddress.setText("Current Address : " + location);
            currentaddress.setTextSize(7 * getResources().getDisplayMetrics().density);

            saveMobileButton=(Button)findViewById(R.id.save_button2);
            saveMobileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    delAddress = (EditText) findViewById(R.id.address);
                    String new_adress = delAddress.getText().toString();

                    boolean check = isNetworkAvailable();
                    if (check == true) {
                        String url = "https://dytila.herokuapp.com/api/changeAddress/" + userid_val + "," + new_adress;
                        Log.e("values", url);
                        new JSONTask().execute(url);
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mobile", new_adress);
                        editor.commit();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            goBack=(ImageView)findViewById(R.id.goback3);
            goBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public class JSONTask extends AsyncTask<String,String ,String> {

        private ProgressDialog dialog = new ProgressDialog(profile_settings.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setTitle("Saving Your Details...");
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
                String status=parentObject.getString("status");

                return status;

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

            if(result.equals("success")){
                Toast.makeText(getApplicationContext(),"Data Saved Successfully!",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(profile_settings.this,features.class);
                startActivity(intent);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
