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
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class askDytila_ask extends AppCompatActivity{

    TextView t1Text;
    ImageView userAvatar,readImg,askImg,ansImg,tagsImg,refreshImg;
    EditText queText;
    Button postButton;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_dytila_ask);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //get shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
        final String username_val = sharedPreferences.getString("username", "");
        final String userid_val = sharedPreferences.getString("user_id", "");
        String mobile_val = sharedPreferences.getString("mobile", "");
        String avatar_val = sharedPreferences.getString("avatar", "");
        String email_val = sharedPreferences.getString("email", "");
        String order_id = sharedPreferences.getString("order_id", "");
        String mealFreq_val = sharedPreferences.getString("mealFreq", "");
        Log.e("values", userid_val);
        //Shared Preferences Ends


        postButton=(Button)findViewById(R.id.postQueButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queText = (EditText) findViewById(R.id.question);
                String que=queText.getText().toString();
                if(que.length()<1){
                    Toast.makeText(getApplicationContext(), "Answer should not be empty!", Toast.LENGTH_SHORT).show();
                }else{
                    boolean check = isNetworkAvailable();
                    if (check == true) {
                        postData(userid_val,username_val,que);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //askDYtila options
        RelativeLayout feedl,askl,ansl,blogl,maccl;
        feedl=(RelativeLayout)findViewById(R.id.img1);
        askl=(RelativeLayout)findViewById(R.id.img2);
        ansl=(RelativeLayout)findViewById(R.id.img3);
        maccl=(RelativeLayout)findViewById(R.id.img4);


        feedl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_ask.this,askDytilaMain.class);
                startActivity(intent);
                finish();
            }
        });
        askl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_ask.this,askDytila_ask.class);
                startActivity(intent);
                finish();
            }
        });
        ansl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_ask.this,askDytila_que.class);
                startActivity(intent);
                finish();
            }
        });
        maccl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_ask.this,Ask_dytila_m_ques_ans.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void postData(final String user_id, final String username,final String que){
        String url="https://dytila.herokuapp.com/api/askDytila/askque";

        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(askDytila_ask.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Question Posted Successfully",Toast.LENGTH_SHORT).show();
                        t1Text=(TextView)findViewById(R.id.t1);
                        t1Text.setText("Ask Another Question");
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
                params.put("user_id",user_id);
                params.put("username",username);
                params.put("que",que);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}