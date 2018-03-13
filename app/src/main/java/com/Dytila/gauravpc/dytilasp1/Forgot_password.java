package com.Dytila.gauravpc.dytilasp1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot_password extends AppCompatActivity {

    String url="https://dytila.herokuapp.com/api/login/forgor_password";
    EditText emailEditText,mobileEditText;
    Button proceedButton;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        proceedButton=(Button)findViewById(R.id.proceed);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEditText=(EditText)findViewById(R.id.email);
                if (!isValidEmail(emailEditText.getText().toString())) {
                    emailEditText.setError("Please Enter A Valid Email Address");
                }
                else{
                    mobileEditText=(EditText)findViewById(R.id.mobile);
                    if(mobileEditText.getText().toString().length()<1){
                        mobileEditText.setError("Please Enter your mobile number");
                    }
                    else{
                        boolean check = isNetworkAvailable();
                        if(check==true){
                            pDialog.show();
                            RequestQueue requestQueue=new Volley().newRequestQueue(Forgot_password.this);
                            StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                pDialog.dismiss();
                                                JSONObject jsonObject=new JSONObject(response);
                                                String status=jsonObject.getString("status");
                                                if(status.equals("password changed")){
                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Forgot_password.this);
                                                    alertDialogBuilder.setMessage("Password changed successfully. Please Login");
                                                    alertDialogBuilder.setPositiveButton("Login Now",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    Intent intent = new Intent(Forgot_password.this, activity1.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            });

                                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();
                                                }else if(status.equals("no mobile number exist")){
                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Forgot_password.this);
                                                    alertDialogBuilder.setMessage("Mobile number not found!");
                                                    alertDialogBuilder.setPositiveButton("Retry",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    Intent intent = new Intent(Forgot_password.this, Forgot_password.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            });
                                                    alertDialogBuilder.setNegativeButton("Canel",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    Intent intent = new Intent(Forgot_password.this, activity1.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            });

                                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();

                                                }else if(status.equals("no email id exist")){
                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Forgot_password.this);
                                                    alertDialogBuilder.setMessage("Email ID not found!");
                                                    alertDialogBuilder.setPositiveButton("Retry",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    Intent intent = new Intent(Forgot_password.this, Forgot_password.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            });
                                                    alertDialogBuilder.setNegativeButton("Canel",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface arg0, int arg1) {
                                                                    Intent intent = new Intent(Forgot_password.this, activity1.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            });

                                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    pDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Something Went Wrong :( Please try again!",Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params=new HashMap<String,String>();
                                    params.put("email",emailEditText.getText().toString());
                                    params.put("mobile",mobileEditText.getText().toString());
                                    return params;
                                }
                            };
                            requestQueue.add(stringRequest);
                        }else{
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
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
