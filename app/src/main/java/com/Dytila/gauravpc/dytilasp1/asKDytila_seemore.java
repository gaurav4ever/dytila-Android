package com.Dytila.gauravpc.dytilasp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class asKDytila_seemore extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView user_name,user_email;
    Toolbar toolbar;
    TextView asked_username,asked_date,asked_question,answered_username,answered_date,answer_text;
    ImageView asked_userImg,answered_userImg;
    private ListView askDytila_seemore;
    Button own_answer_button;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_as_kdytila_seemore);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Posting your Answer");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        final String username_val=sharedPreferences.getString("username", "");
        final String userid_val=sharedPreferences.getString("user_id", "");
        String mobile_val=sharedPreferences.getString("mobile","");
        String avatar_val=sharedPreferences.getString("avatar","");
        String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String mealFreq_val=sharedPreferences.getString("mealFreq","");
        Log.e("values", userid_val);
        //Shared Preferences Ends

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        //puting data to navigation header
        user_name=(TextView)header.findViewById(R.id.username);
        user_email=(TextView)header.findViewById(R.id.user_email);
        user_name.setText(username_val);
        user_email.setText(email_val);
        //Navigation Operation Ends

        final Intent intent=getIntent();
        final String que_id=intent.getStringExtra("que_id");
        final String username1=intent.getStringExtra("username_who_asked");
        String date1=intent.getStringExtra("date1");
        final String que=intent.getStringExtra("que");
        String img1=intent.getStringExtra("img1");
        String username2=intent.getStringExtra("username_who_answered");
        String date2=intent.getStringExtra("date2");
        String ans=intent.getStringExtra("ans");
        String img2=intent.getStringExtra("img2");

        asked_username=(TextView)findViewById(R.id.username_who_asked);
        asked_date=(TextView)findViewById(R.id.date1);
        asked_question=(TextView)findViewById(R.id.question);

        asked_date.setText(date1);
        asked_username.setText(username1);
        asked_question.setText(que);

        asked_userImg=(ImageView)findViewById(R.id.user_who_asked_avatarImg);
        ImageLoader.getInstance().displayImage(img1, asked_userImg);

        CardView giveAnswer_card=(CardView)findViewById(R.id.giveAnswerCard);
        final RelativeLayout answer_rl=(RelativeLayout)findViewById(R.id.answerArea);
        answer_rl.setVisibility(View.GONE);
        final int[] flag1 = {0};
        giveAnswer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView seemoreImg=(ImageView)findViewById(R.id.see_more);
                if(flag1[0] ==0) {
                    answer_rl.setVisibility(View.VISIBLE);
                    seemoreImg.setImageResource(R.drawable.minus);
                    flag1[0]=1;
                }
                else{
                    answer_rl.setVisibility(View.GONE);
                    seemoreImg.setImageResource(R.drawable.add);
                    flag1[0]=0;
                }
            }
        });

        own_answer_button=(Button)findViewById(R.id.postButton);
        own_answer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=(EditText)findViewById(R.id.answer);
                String usernameVal=username_val;
                String user_idVal=userid_val;
                String que_idVal=que_id;
                String ansVal=editText.getText().toString();
                boolean check = isNetworkAvailable();
                if (check == true) {
                    postData(user_idVal,usernameVal,que_idVal,ansVal);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        answered_username=(TextView)findViewById(R.id.username_who_answered);
        answered_date=(TextView)findViewById(R.id.date2);
        answer_text=(TextView)findViewById(R.id.answerText);
        answered_userImg=(ImageView)findViewById(R.id.user_who_answered_avatarImg);

        answered_username.setText(username2);
        answered_date.setText(date2);
        answer_text.setText(ans);
        ImageLoader.getInstance().displayImage(img2, answered_userImg);

        RelativeLayout seeAllAnswers=(RelativeLayout)findViewById(R.id.see_all_answers);
        seeAllAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(asKDytila_seemore.this,AskDytila_allAnswers.class);
                intent1.putExtra("que_id",que_id);
                intent1.putExtra("que",que);
                startActivity(intent1);
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
        String url="https://dytila.herokuapp.com/api/askDytila/ans";

        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(asKDytila_seemore.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Answer Posted Successfully",Toast.LENGTH_SHORT).show();
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
                params.put("que_id",que_id);
                params.put("ans",ans);
                return params;
            }
        };
        requestQueue.add(stringRequest);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.home_id){
            Intent intent=new Intent(asKDytila_seemore.this,features.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.bill_id){
            Intent intent=new Intent(asKDytila_seemore.this,bills.class);
            startActivity(intent);
        }
        else if(id==R.id.account_id){
            Intent intent=new Intent(asKDytila_seemore.this,account.class);
            startActivity(intent);
        }
        else if(id==R.id.order_id){
            Intent intent=new Intent(asKDytila_seemore.this,Order_details.class);
            startActivity(intent);
        }
        else if(id==R.id.renew_id){
            Intent intent=new Intent(asKDytila_seemore.this,renew.class);
            startActivity(intent);
        }
        else if(id==R.id.logout_id){
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
            Intent intent=new Intent(asKDytila_seemore.this,activity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (id == R.id.askDytila_id) {
            Intent intent=new Intent(asKDytila_seemore.this,askDytilaMain.class);
            startActivity(intent);

        }else if (id == R.id.slabs_id) {
            Intent intent=new Intent(asKDytila_seemore.this,slabs.class);
            startActivity(intent);

        }else if (id == R.id.facebook_id) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/167551100331762"));
            startActivity(intent);

        } else if (id == R.id.instagram_id) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dytila_nutrition"));
            startActivity(intent);
        } else if (id == R.id.aboutus_id) {

            Intent intent=new Intent(asKDytila_seemore.this,about_us.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("menu option", "Menu Made");
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

}
