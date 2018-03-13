package com.Dytila.gauravpc.dytilasp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

public class askDytila_newAns extends AppCompatActivity {

    TextView ask_username,ask_date,ask_que;
    ImageView ask_avatar;
    EditText ans_text;
    Button post_button;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_dytila_new_ans);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Posting you Answer");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent intent=getIntent();
        final String que_id=intent.getStringExtra("que_id");
        String name=intent.getStringExtra("name");
        String date=intent.getStringExtra("date");
        String avatar=intent.getStringExtra("avatar");
        String que=intent.getStringExtra("que");

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
        final String username_val = sharedPreferences.getString("username", "");
        final String userid_val = sharedPreferences.getString("user_id", "");

        ask_username=(TextView)findViewById(R.id.username_who_asked);
        ask_date=(TextView)findViewById(R.id.date);
        ask_que=(TextView)findViewById(R.id.question);
        ask_avatar=(ImageView)findViewById(R.id.user_who_asked_avatarImg);

        ask_username.setText(name);
        ask_date.setText(date);
        ask_que.setText(que);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        ImageLoader.getInstance().displayImage(avatar, ask_avatar);

        post_button=(Button)findViewById(R.id.postButton);
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans_text=(EditText)findViewById(R.id.answer);
                String ans=ans_text.getText().toString();
                if(ans.length()<1){
                    Toast.makeText(getApplicationContext(), "Answer should not be empty!", Toast.LENGTH_SHORT).show();
                }else{
                    boolean check = isNetworkAvailable();
                    if (check == true) {
                        postData(userid_val,username_val,que_id,ans);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
    public void postData(final String user_id, final String username, final String que_id, final String ans){
        String url="https://dytila.herokuapp.com/api/askDytila/newans";

        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(askDytila_newAns.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Answer Posted Successfully", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        Intent intent=new Intent(askDytila_newAns.this,askDytilaMain.class);
                        startActivity(intent);
                        finish();
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
                params.put("que_id",que_id);
                params.put("ans",ans);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
