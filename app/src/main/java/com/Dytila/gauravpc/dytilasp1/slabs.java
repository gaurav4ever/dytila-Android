package com.Dytila.gauravpc.dytilasp1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class slabs extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ImageView goBack;
    EditText nameText,emailText,mobileText,addressText;
    Button submitButton;
    Spinner sizeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slabs);

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        final String username_val=sharedPreferences.getString("username", "");
        final String mobile_val=sharedPreferences.getString("mobile", "");
        final String email_val=sharedPreferences.getString("email","");
        final String location_val=sharedPreferences.getString("location","");

        goBack=(ImageView)findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameText=(EditText)findViewById(R.id.fullname);
        nameText.setText(username_val);
        emailText=(EditText)findViewById(R.id.email);
        emailText.setText(email_val);
        mobileText=(EditText)findViewById(R.id.mobile);
        mobileText.setText(mobile_val);
        addressText=(EditText)findViewById(R.id.address);
        addressText.setText(location_val);

        sizeSpinner=(Spinner)findViewById(R.id.size);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.size,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);
        sizeSpinner.setOnItemSelectedListener(this);

        submitButton=(Button)findViewById(R.id.orderButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Strength labs Coming Soon :)", Toast.LENGTH_SHORT).show();
//                String size_val=sizeSpinner.getSelectedItem().toString();
////                Log.e("size : ",size_val);
//
//                String usernameText = username_val.replaceAll(" ", "%20");
//                String LocationText=location_val.replaceAll(" ","%20");
//                String sizeText=size_val.replaceAll(" ","%20");
//
//                boolean check=isNetworkAvailable();
//                if(check==true) {
//                    String url="https://dytila.herokuapp.com/api/slabsOrders/"+usernameText+","+email_val+","+mobile_val+","+sizeText+","+LocationText;
////                    Log.e("values", url);
//                    new JSONTask().execute(url);
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class JSONTask extends AsyncTask<String,String ,String> {

        private ProgressDialog dialog = new ProgressDialog(slabs.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setTitle("Processing ...");
            this.dialog.setMessage("Please Wait ...");
            this.dialog.show();
            final JSONTask asyncObject = this;
            new CountDownTimer(10000, 10000) {
                public void onTick(long millisUntilFinished) {
                    // monitor the progress
                }
                public void onFinish() {
                    // stop async task if not in progress
                    if (asyncObject.getStatus() == Status.RUNNING) {
                        asyncObject.cancel(false);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(slabs.this);
                        alertDialogBuilder.setMessage("Connection Is Unstable!");
                        alertDialogBuilder.setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(slabs.this, slabs.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(slabs.this, features.class);
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

//            Log.e("status",result);

           if(result.equals("success")){
               Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_SHORT).show();
           }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
