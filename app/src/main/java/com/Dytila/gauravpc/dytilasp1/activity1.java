package com.Dytila.gauravpc.dytilasp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity1 extends AppCompatActivity {

    EditText email,password;
    Button loginButton;
    TextView register,skipNowText;
    String isLogin="0";
    char c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity1);

        final TextView forgotPasswordText=(TextView)findViewById(R.id.forgotPassword);
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity1.this,Forgot_password.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(this.getResources().getColor(R.color.loginScreenColor,null));
            }
        }

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
            Intent intent=new Intent(activity1.this,features.class);
            startActivity(intent);
        }

        loginButton=(Button)findViewById(R.id.loginButton);
        register=(TextView)findViewById(R.id.register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                email = (EditText) findViewById(R.id.email);
                if (!isValidEmail(email.getText().toString())) {
                    email.setError("Please Enter A Valid Email Address");
                } else {
                    password = (EditText) findViewById(R.id.password);
                    if (password.getText().toString().length() < 1) {
                        password.setError("Please enter your password");
                    } else {
                        String email_val = email.getText().toString();
                        String pass = password.getText().toString();

                        boolean check = isNetworkAvailable();
                        if (check == true) {
                            login(email_val, pass);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity1.this, activity2.class);
                startActivity(intent);
            }
        });

    }
    public void login(final String email,final String pass){
        final String url="https://dytila.herokuapp.com/api/login";
        final ProgressDialog loading=ProgressDialog.show(this,"Loggin You In...","please wait...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        String name =null,id=null,avatar=null,mobile=null,email=null,status = null,order_id=null;

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            name = jsonObject.getString("name");
                            id=jsonObject.getString("id");
                            avatar=jsonObject.getString("avatar");
                            mobile=jsonObject.getString("mobile");
                            email=jsonObject.getString("email");
                            status=jsonObject.getString("status");
                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("incorrect password")){
                            Toast.makeText(getApplicationContext(),"Password Incorrect!",Toast.LENGTH_SHORT).show();
                        }
                        else  if(status.equals("user does not exist")){
                            Toast.makeText(getApplicationContext(),"Account doesn't exist!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("username",upperFirst(name));
                            editor.putString("user_id",id);
                            editor.putString("mobile",mobile);
                            editor.putString("avatar",avatar);
                            editor.putString("email",email);
                            editor.putString("order_id","null");
                            editor.putString("mealName","null");
                            editor.putString("location", "Not Specified");
                            editor.putString("TmealFreq","null");
                            editor.putString("mealFreq","null");
                            editor.putString("timing","null");
                            editor.putString("specs","null");
                            editor.putString("mKitchen","null");
                            editor.putString("firstTime","0");
                            editor.apply();

                            //saving session to internal storage file
                            String loginMsg="1";
                            try {
                                FileOutputStream fileOutputStream= openFileOutput("Dytila Login",MODE_PRIVATE);
                                fileOutputStream.write(loginMsg.getBytes());
                                fileOutputStream.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Intent intent=new Intent(activity1.this,features.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Some Error Occurred :(",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("email",email);
                params.put("password",pass);
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
