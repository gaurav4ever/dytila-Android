package com.Dytila.gauravpc.dytilasp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity2 extends AppCompatActivity {

    EditText firstName,lastName,emailId,password,rePassword,mobile;
    Button register;
    String reg_values="";
    String isLogin="0";
    char c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);

        //checking if session is set
        try {
            String msg;
            FileInputStream fileInputStream=openFileInput("Dytila Login");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();
            while((msg=bufferedReader.readLine())!=null){
                stringBuffer.append(msg);
            }

//            Log.e("session",stringBuffer.toString());
            String val=stringBuffer.toString();
            int len=val.length();
            Log.e("session",val+" "+len);
            c=val.charAt(len-1);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(c=='1'){
            Intent intent=new Intent(activity2.this,MainActivity.class);
            startActivity(intent);
        }

        register=(Button)findViewById(R.id.registerButton);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int flag = 0;
                    firstName = (EditText) findViewById(R.id.firstname);
                    if (firstName.getText().toString().length() < 1) {
                        firstName.setError("Please Enter First Name");
                        flag--;
                    }
                    lastName = (EditText) findViewById(R.id.lastname);
                    if (lastName.getText().toString().length() < 1) {
                        lastName.setError("Please Enter First Name");
                        flag--;
                    }
                    emailId = (EditText) findViewById(R.id.email);
                    if (!isValidEmail(emailId.getText().toString())) {
                        emailId.setError("Please Enter A Valid Email Address");
                        flag--;
                    }
                    mobile = (EditText) findViewById(R.id.mobile);
                    if (!(mobile.getText().toString().length() == 10)) {
                        mobile.setError("Mobile Number Must Be Of 10 Digits");
                        flag--;
                    }
                    password = (EditText) findViewById(R.id.password);
                    if (password.getText().toString().length() < 5) {
                        password.setError("Password must be at least of 5 character");
                        flag--;
                    }
                    rePassword = (EditText) findViewById(R.id.repassword);
                    if (!(password.getText().toString().equals(rePassword.getText().toString()))) {
                        rePassword.setError("Password did not match with above Password!");
                        flag--;
                    }


                    if (flag >= 0) {
                        String user_firstname = firstName.getText().toString();
                        String user_lastname = lastName.getText().toString();
                        String user_email = emailId.getText().toString();
                        String user_mobile = mobile.getText().toString();
                        String user_pass = password.getText().toString();
                        String user_rePass = rePassword.getText().toString();


                        reg_values = user_firstname + "," + user_lastname + "," + user_email + "," + user_mobile + "," + user_pass;
                        boolean check = isNetworkAvailable();
                        if (check == true) {
                            register(user_firstname, user_lastname, user_email, user_mobile, user_pass);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });



    }
    public void register(final String user_firstname,final String user_lastname,final String user_email,final String user_mobile,final String user_pass){
        final String url="https://dytila.herokuapp.com/api/register";
        final ProgressDialog loading=ProgressDialog.show(this,"Registering...","please wait...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();

                        String user_id = null;

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            user_id=jsonObject.getString("user_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(user_id.equals("null")){
                            Toast.makeText(getApplicationContext(),"Account Already exist!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("username",upperFirst(user_firstname+" "+user_lastname));
                            editor.putString("user_id",user_id);
                            editor.putString("mobile",user_mobile);
                            editor.putString("email",user_email);
                            editor.putString("order_id","null");
                            editor.putString("mealName","null");
                            editor.putString("avatar","https://dytila.herokuapp.com/static/img/customer_img/default.png");
                            editor.putString("location", "Not Specified");
                            editor.putString("TmealFreq","null");
                            editor.putString("mealFreq","null");
                            editor.putString("timing","null");
                            editor.putString("specs","null");
                            editor.putString("firstTime","0");
                            editor.apply();

                            Toast.makeText(getApplicationContext(),"Successfully registered",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(activity2.this,features.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Some Error Occurred!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("user_firstname",user_firstname);
                params.put("user_lastname",user_lastname);
                params.put("user_email",user_email);
                params.put("user_mobile",user_mobile);
                params.put("user_mobile",user_mobile);
                params.put("user_pass",user_pass);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        int socketTimeout = 20000;//20 seconds timeout
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
