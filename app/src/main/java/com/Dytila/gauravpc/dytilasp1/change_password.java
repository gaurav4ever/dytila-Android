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

public class change_password extends AppCompatActivity {

    EditText oldPassText,newPass1Text,newPass2Text;
    Button button;
    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button=(Button)findViewById(R.id.button);

        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo",Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        final String userid_val=sharedPreferences.getString("user_id", "");
        String mobile_val=sharedPreferences.getString("mobile","");
        String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        Log.e("values", userid_val);
        //Shared Preferences Ends

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassText=(EditText)findViewById(R.id.oldPass);
                newPass1Text=(EditText)findViewById(R.id.newPass1);
                newPass2Text=(EditText)findViewById(R.id.newPass2);
                String op=oldPassText.getText().toString();
                String np1=newPass1Text.getText().toString();
                String np2=newPass2Text.getText().toString();
                if(np1.equals(np2)){
                    boolean check=isNetworkAvailable();
                    if(check==true) {
                        String url="https://dytila.herokuapp.com/api/changePass/"+userid_val+","+op+","+np1;
                        Log.e("values", url);
                        new JSONTask().execute(url);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(change_password.this,features.class);
                        startActivity(intent);
                    }
                }
                else if(op.equals(np1)){
                    Toast.makeText(getApplicationContext(), "Old and New password Cannot be Same!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password did not match!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBack=(ImageView)findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(change_password.this,account.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        private ProgressDialog dialog = new ProgressDialog(change_password.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setTitle("Refreshing ...");
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
                Toast.makeText(getApplicationContext(),"Password Changed Successfully!",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(change_password.this,features.class);
                startActivity(intent);
            }

        }
    }
}
