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
import android.view.View;
import android.widget.Button;
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

public class renew extends AppCompatActivity {

    ImageView cross_goBack;
    Button renewButton,newOrderButton;
    TextView orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        final String userid_val=sharedPreferences.getString("user_id", "");
        final String order_id=sharedPreferences.getString("order_id", "");
        final String mealFreq_val=sharedPreferences.getString("mealFreq","");
        final String TmealFreq_val=sharedPreferences.getString("TmealFreq","");

        orderStatus=(TextView)findViewById(R.id.order_status);
        newOrderButton=(Button)findViewById(R.id.newOrder);
        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(renew.this,features.class);
                startActivity(intent);
            }
        });

        renewButton=(Button)findViewById(R.id.renew_button);
        renewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(order_id.equals("null")){
                    orderStatus.setText("You have not order anything yet :(");
                    orderStatus.setTextSize(9 * getResources().getDisplayMetrics().density);
                }
                else if(!order_id.equals("null") && mealFreq_val.equals("0")){

                    boolean check=isNetworkAvailable();
                    if(check==true) {
                        new JSONTask().execute("https://dytila.herokuapp.com/api/renew/"+order_id+","+userid_val);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(renew.this,features.class);
                        startActivity(intent);
                    }
                }
                else {
                    orderStatus.setText("Your "+TmealFreq_val+" days meal plan is not over yet!");
                    orderStatus.setTextSize(9* getResources().getDisplayMetrics().density);
                }

            }
        });

        cross_goBack=(ImageView)findViewById(R.id.cross);
        cross_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        private ProgressDialog dialog = new ProgressDialog(renew.this);
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.account_details);
        @Override
        protected void onPreExecute() {

            this.dialog.setTitle("Checking Meal Frequency ...");
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

            orderStatus.setVisibility(View.VISIBLE);

            if(result.equals("renewed")){
                orderStatus.setText("Order Renewed :)");
            }
            else{
                orderStatus.setText("Your meal plan is not over yet!");
            }

        }
    }

    @Override
    public void onBackPressed() {
        if(orderStatus.getText().toString().equals("Order Renewed :)")){
            Toast.makeText(getApplicationContext(),"Please Refresh",Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }
}

