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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Dytila.gauravpc.dytilasp1.models.AskDytila_ques;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class askDytila_que extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView user_name, user_email;
    Toolbar toolbar;
    ImageView userAvatar, readImg, askImg, ansImg, tagsImg, refreshImg;
    private ListView ques;
    RelativeLayout r,no_connection_layout;
    int asyncTaskDone=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_dytila_que);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val = sharedPreferences.getString("username", "");
        String userid_val = sharedPreferences.getString("user_id", "");
        String mobile_val = sharedPreferences.getString("mobile", "");
        String avatar_val = sharedPreferences.getString("avatar", "");
        String email_val = sharedPreferences.getString("email", "");
        String order_id = sharedPreferences.getString("order_id", "");
        String mealFreq_val = sharedPreferences.getString("mealFreq", "");
        Log.e("values", userid_val);
        //Shared Preferences Ends

        //Navigation Operation Begins
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,account.class);
                startActivity(intent);
            }
        });
        user_name=(TextView)findViewById(R.id.username);
        user_email=(TextView)findViewById(R.id.user_email);
        user_name.setText(username_val);
        user_email.setText(email_val);
        userAvatar=(ImageView)findViewById(R.id.userImg);
        ImageLoader.getInstance().displayImage(avatar_val, userAvatar);
        LinearLayout homeLayout,accountLayout,myOrdersLayout,billsLayout,renewLayout,logoutLayout,aboutUsLayout;
        homeLayout=(LinearLayout)findViewById(R.id.home);
        accountLayout=(LinearLayout)findViewById(R.id.account);
        myOrdersLayout=(LinearLayout)findViewById(R.id.my_orders);
        billsLayout=(LinearLayout)findViewById(R.id.bills);
        renewLayout=(LinearLayout)findViewById(R.id.renew);
        logoutLayout=(LinearLayout)findViewById(R.id.logout);
        aboutUsLayout=(LinearLayout)findViewById(R.id.aboutus);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,account.class);
                startActivity(intent);
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(askDytila_que.this, Order_details.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(askDytila_que.this, bills.class);
                startActivity(intent);
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,renew.class);
                startActivity(intent);
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
                sharedPreferences.edit().clear().commit();
                String loginMsg = "0";
                try {
                    FileOutputStream fileOutputStream = openFileOutput("Dytila Login", MODE_APPEND);
                    fileOutputStream.write(loginMsg.getBytes());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(askDytila_que.this, activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

        ques = (ListView) findViewById(R.id.askDytila_unanswered_ques);
        ques.setVisibility(View.GONE);
        r=(RelativeLayout)findViewById(R.id.loadingLayout);
        r.setVisibility(View.VISIBLE);
        no_connection_layout=(RelativeLayout)findViewById(R.id.no_connectionLayout);
        no_connection_layout.setVisibility(View.GONE);

        boolean check = isNetworkAvailable();
        if (check == true) {
            new JSONTask().execute("https://dytila.herokuapp.com/api/askDytila/ques");
        } else {
            no_connection_layout.setVisibility(View.VISIBLE);
            r.setVisibility(View.GONE);
            Button retry_button=(Button)findViewById(R.id.retryButton);
            retry_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(askDytila_que.this, askDytila_que.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        //askDYtila options
        RelativeLayout feedl,askl,ansl,blogl,maccl;
        feedl=(RelativeLayout)findViewById(R.id.img1);
        askl=(RelativeLayout)findViewById(R.id.img2);
        ansl=(RelativeLayout)findViewById(R.id.img3);
        maccl=(RelativeLayout)findViewById(R.id.img4);


        feedl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,askDytilaMain.class);
                startActivity(intent);
                finish();
            }
        });
        askl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,askDytila_ask.class);
                startActivity(intent);
                finish();
            }
        });
        ansl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,askDytila_que.class);
                startActivity(intent);
                finish();
            }
        });
        maccl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytila_que.this,Ask_dytila_m_ques_ans.class);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("menu option", "Menu Made");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    public class JSONTask extends AsyncTask<String,String ,List<AskDytila_ques>> {

        private ProgressDialog dialog = new ProgressDialog(askDytila_que.this);

        @Override
        protected void onPreExecute() {

            r.setVisibility(View.VISIBLE);
            //timeout for Async Task
            final JSONTask asyncObject = this;
            new CountDownTimer(10000, 10000) {
                public void onTick(long millisUntilFinished) {
                    // monitor the progress
                    if(asyncTaskDone==1) {
                        Toast.makeText(getApplicationContext(), "Im done", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("Timer Status ","" + millisUntilFinished / 1000);
                }
                public void onFinish() {
                    // stop async task if not in progress
                    if (asyncObject.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncObject.cancel(false);
                        r.setVisibility(View.GONE);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(askDytila_que.this);
                        alertDialogBuilder.setMessage("Connection Is Unstable!");
                        alertDialogBuilder.setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(askDytila_que.this, askDytilaMain.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(askDytila_que.this, features.class);
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
        protected List<AskDytila_ques> doInBackground(String... params) {
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
                JSONArray parentArray=parentObject.getJSONArray("all");

                List<AskDytila_ques> askDytila_quesList=new ArrayList<>();
                for(int i=0;i<parentArray.length();i++){

                    JSONObject finalObject=parentArray.getJSONObject(i);
                    AskDytila_ques askDytila_ques=new AskDytila_ques();
                    askDytila_ques.setQue_id(finalObject.getString("que_id"));
                    askDytila_ques.setQue(finalObject.getString("ques"));
                    askDytila_ques.setAskedDate(finalObject.getString("que_date"));
                    askDytila_ques.setAsked_by_user(finalObject.getString("askedBy"));
                    askDytila_ques.setUser_who_asked_avatar(finalObject.getString("user_who_asked_avatar"));
                    askDytila_quesList.add(askDytila_ques);

                }
                //adding the final obejct in the list

                return  askDytila_quesList;

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
        protected void onPostExecute(List<AskDytila_ques> askDytila_ques) {
            super.onPostExecute(askDytila_ques);

            ques.setVisibility(View.VISIBLE);
            r.setVisibility(View.GONE);

            AskDytila_quesAdapter askDytila_quesAdapter=new AskDytila_quesAdapter(getApplicationContext(),R.layout.row_askdytila_unanswered_ques,askDytila_ques);
            ques.setAdapter(askDytila_quesAdapter);

        }
    }

    public class AskDytila_quesAdapter extends ArrayAdapter {

        public List<AskDytila_ques> askDytila_quesModelList;
        private int resource;

        private LayoutInflater inflater;

        public AskDytila_quesAdapter(Context context, int resource, List<AskDytila_ques> objects) {
            super(context, resource, objects);
            askDytila_quesModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.row_askdytila_unanswered_ques,null);
            }

            final TextView username1text,queText,date1;
            ImageView img1;
            username1text=(TextView)convertView.findViewById(R.id.username_who_asked);
            queText=(TextView)convertView.findViewById(R.id.question);
            date1=(TextView)convertView.findViewById(R.id.date);

            final String username_who_askedText = upperFirst(askDytila_quesModelList.get(position).getAsked_by_user());
            username1text.setText(username_who_askedText);
            final String question=makeFirstUpper(askDytila_quesModelList.get(position).getQue());
            queText.setText(question);
            date1.setText("on "+askDytila_quesModelList.get(position).getAskedDate());
            img1=(ImageView)convertView.findViewById(R.id.user_who_asked_avatarImg);

            ImageLoader.getInstance().displayImage(askDytila_quesModelList.get(position).getUser_who_asked_avatar(), img1);

            queText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(askDytila_que.this,askDytila_newAns.class);
                    intent.putExtra("que_id",askDytila_quesModelList.get(position).getQue_id());
                    intent.putExtra("name",username_who_askedText);
                    intent.putExtra("date","on "+askDytila_quesModelList.get(position).getAskedDate());
                    intent.putExtra("que",question);
                    intent.putExtra("avatar",askDytila_quesModelList.get(position).getUser_who_asked_avatar());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
    public String makeFirstUpper(String val){
        String final_answer="";
        final_answer+=String.valueOf(val.charAt(0)).toUpperCase();
        final_answer+=val.substring(1);
        return final_answer;
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
