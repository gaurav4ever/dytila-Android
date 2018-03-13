package com.Dytila.gauravpc.dytilasp1;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.Dytila.gauravpc.dytilasp1.models.AskDytila_all;
import com.Dytila.gauravpc.dytilasp1.models.Likesmodel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class askDytilaMain extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView user_name,user_email,noLikesText;
    Toolbar toolbar;
    ImageView userAvatar,readImg,askImg,ansImg,tagsImg,refreshImg;
    private ListView all;
    ListView likesListView;
    RelativeLayout r,no_connection_layout,noLikesLayout,loadingLayout,answerLayout;
    int asyncTaskDone=0;
    String user_id,username;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_dytila_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        String userid_val=sharedPreferences.getString("user_id", "");
        String mobile_val=sharedPreferences.getString("mobile","");
        String avatar_val=sharedPreferences.getString("avatar","");
        String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String mealFreq_val=sharedPreferences.getString("mealFreq","");
        Log.e("values", userid_val);
        user_id=userid_val;username=username_val;
        //Shared Preferences Ends

        //Navigation Operation Begins
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytilaMain.this,account.class);
                startActivity(intent);
            }
        });
        user_name=(TextView)findViewById(R.id.username);
        user_email=(TextView)findViewById(R.id.user_email);
        user_name.setText(username_val);
        user_email.setText(email_val);
        userAvatar=(ImageView)findViewById(R.id.userImg);
        ImageLoader.getInstance().displayImage(avatar_val, userAvatar);
        LinearLayout homeLayout,accountLayout,myOrdersLayout,billsLayout,renewLayout,logoutLayout,aboutUsLayout,favMealsLayout;
        homeLayout=(LinearLayout)findViewById(R.id.home);
        accountLayout=(LinearLayout)findViewById(R.id.account);
        myOrdersLayout=(LinearLayout)findViewById(R.id.my_orders);
        favMealsLayout=(LinearLayout)findViewById(R.id.fav_meals);
        billsLayout=(LinearLayout)findViewById(R.id.bills);
        renewLayout=(LinearLayout)findViewById(R.id.renew);
        logoutLayout=(LinearLayout)findViewById(R.id.logout);
        aboutUsLayout=(LinearLayout)findViewById(R.id.aboutus);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytilaMain.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytilaMain.this,account.class);
                startActivity(intent);
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(askDytilaMain.this, Order_details.class);
                startActivity(intent);
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(askDytilaMain.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(askDytilaMain.this, bills.class);
                startActivity(intent);
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytilaMain.this,renew.class);
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
                Intent intent = new Intent(askDytilaMain.this, activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytilaMain.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

        all = (ListView) findViewById(R.id.askDytila_all);
        all.setVisibility(View.GONE);
        r=(RelativeLayout)findViewById(R.id.loadingLayout);
        r.setVisibility(View.VISIBLE);
        no_connection_layout=(RelativeLayout)findViewById(R.id.no_connectionLayout);
        no_connection_layout.setVisibility(View.GONE);

        boolean check=isNetworkAvailable();
        if(check==true) {
            new JSONTask().execute("https://dytila.herokuapp.com/api/askDytila/all/"+userid_val);
        }
        else{
            no_connection_layout.setVisibility(View.VISIBLE);
            r.setVisibility(View.GONE);
            Button retry_button=(Button)findViewById(R.id.retryButton);
            retry_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(askDytilaMain.this, askDytilaMain.class);
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
                Intent intent=new Intent(askDytilaMain.this,askDytilaMain.class);
                startActivity(intent);
                finish();
            }
        });
        askl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytilaMain.this,askDytila_ask.class);
                startActivity(intent);
            }
        });
        ansl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytilaMain.this,askDytila_que.class);
                startActivity(intent);
            }
        });
        maccl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(askDytilaMain.this,Ask_dytila_m_ques_ans.class);
                startActivity(intent);
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onBackPressed() {

        Intent intent=new Intent(askDytilaMain.this,features.class);
        startActivity(intent);
        finish();

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
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    public class JSONTask extends AsyncTask<String,String ,List<AskDytila_all>> {

        private ProgressDialog dialog = new ProgressDialog(askDytilaMain.this);

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
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(askDytilaMain.this);
                        alertDialogBuilder.setMessage("Connection Is Unstable!");
                        alertDialogBuilder.setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(askDytilaMain.this, askDytilaMain.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(askDytilaMain.this, features.class);
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
        protected List<AskDytila_all> doInBackground(String... params) {
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

                List<AskDytila_all> askDytila_allList=new ArrayList<>();
                for(int i=0;i<parentArray.length();i++){

                    JSONObject finalObject=parentArray.getJSONObject(i);
                    AskDytila_all askDytila_all=new AskDytila_all();
                    askDytila_all.setQue_id(finalObject.getString("que_id"));
                    askDytila_all.setQue(finalObject.getString("ques"));
                    askDytila_all.setAns(finalObject.getString("ans"));
                    askDytila_all.setAns_id(finalObject.getString("ans_id"));
                    askDytila_all.setAskedDate(finalObject.getString("que_date"));
                    askDytila_all.setAnsweredDate(finalObject.getString("ans_date"));
                    askDytila_all.setAsked_by_user(finalObject.getString("askedBy"));
                    askDytila_all.setAnswered_by_user(finalObject.getString("answeredBy"));
                    askDytila_all.setUser_who_asked_avatar(finalObject.getString("user_who_asked_avatar"));
                    askDytila_all.setUser_who_answered_avatar(finalObject.getString("user_who_answered_avatar"));
                    askDytila_all.setLikesCount(finalObject.getString("likesCount"));
                    askDytila_all.setLiked(finalObject.getString("liked"));
                    askDytila_allList.add(askDytila_all);

                }
                //adding the final obejct in the list

                return  askDytila_allList;

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
        protected void onPostExecute(List<AskDytila_all> askDytila_all) {
            super.onPostExecute(askDytila_all);

            asyncTaskDone=1;
            r.setVisibility(View.GONE);
            all.setVisibility(View.VISIBLE);

            AskDytila_allAdapter askDytila_allAdapter=new AskDytila_allAdapter(getApplicationContext(),R.layout.row_bill,askDytila_all);
            all.setAdapter(askDytila_allAdapter);

        }
    }

    public class AskDytila_allAdapter extends ArrayAdapter {

        public List<AskDytila_all> askDytila_allModelList;
        private int resource;

        private LayoutInflater inflater;

        public AskDytila_allAdapter(Context context, int resource, List<AskDytila_all> objects) {
            super(context, resource, objects);
            askDytila_allModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.row_ask_dytila_que,null);
            }

            final TextView username1text,username2Text,queText,ansText,date1,date2,likesCountText;
            ImageView img1,img2;
            username1text=(TextView)convertView.findViewById(R.id.username_who_asked);
            username2Text=(TextView)convertView.findViewById(R.id.username_who_answered);
            queText=(TextView)convertView.findViewById(R.id.question);
            ansText=(TextView)convertView.findViewById(R.id.answer);
            date1=(TextView)convertView.findViewById(R.id.date1);
            date2=(TextView)convertView.findViewById(R.id.date2);

            final String username_who_askedText=upperFirst(askDytila_allModelList.get(position).getAsked_by_user());
            String username_who_answeredText=upperFirst(askDytila_allModelList.get(position).getAnswered_by_user());

            username1text.setText(username_who_askedText);
            username2Text.setText(username_who_answeredText);

            final String question=makeFirstUpper(askDytila_allModelList.get(position).getQue());
            final String answer=makeFirstUpper(askDytila_allModelList.get(position).getAns());

            queText.setText(question);
            if(answer.length()>200)
                ansText.setText(answer.substring(0, 200)+"...SEE MORE");
            else
                ansText.setText(answer);

            date1.setText("on "+askDytila_allModelList.get(position).getAskedDate());
            date2.setText("on "+askDytila_allModelList.get(position).getAnsweredDate());

            img1=(ImageView)convertView.findViewById(R.id.user_who_asked_avatarImg);
            img2=(ImageView)convertView.findViewById(R.id.user_who_answered_avatarImgavatarImg);

            ImageLoader.getInstance().displayImage(askDytila_allModelList.get(position).getUser_who_asked_avatar(), img1);
            ImageLoader.getInstance().displayImage(askDytila_allModelList.get(position).getUser_who_answered_avatar(), img2);

            ansText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String que_id = askDytila_allModelList.get(position).getQue_id();
                    String que = question;
                    Intent intent1=new Intent(askDytilaMain.this,AskDytila_allAnswers.class);
                    intent1.putExtra("que_id", que_id);
                    intent1.putExtra("que", que);
                    intent1.putExtra("selectedPos", String.valueOf(position));
                    startActivityForResult(intent1, 1);
//                    String que_id = askDytila_allModelList.get(position).getQue_id();
//                    String username_who_asked = username_who_askedText;
//                    String username_who_answered = askDytila_allModelList.get(position).getAnswered_by_user();
//                    String date1 = askDytila_allModelList.get(position).getAskedDate();
//                    String date2 = askDytila_allModelList.get(position).getAnsweredDate();
//                    String que = question;
//                    String ans = answer;
//                    String img1 = askDytila_allModelList.get(position).getUser_who_asked_avatar();
//                    String img2 = askDytila_allModelList.get(position).getUser_who_answered_avatar();
//                    Intent intent = new Intent(askDytilaMain.this, asKDytila_seemore.class);
//                    intent.putExtra("que_id", que_id);
//                    intent.putExtra("username_who_asked", username_who_asked);
//                    intent.putExtra("username_who_answered", username_who_answered);
//                    intent.putExtra("date1", date1);
//                    intent.putExtra("date2", date2);
//                    intent.putExtra("que", que);
//                    intent.putExtra("ans", ans);
//                    intent.putExtra("img1", img1);
//                    intent.putExtra("img2", img2);
//                    startActivity(intent);
                }
            });

            //like and comment system
            likesCountText=(TextView)convertView.findViewById(R.id.likesCount);
            likesCountText.setText(askDytila_allModelList.get(position).getLikesCount());

            RelativeLayout likeArea,commentArea,likeCountArea,commentCountArea;
            likeArea=(RelativeLayout)convertView.findViewById(R.id.likeArea);

            likeCountArea=(RelativeLayout)convertView.findViewById(R.id.likeCoundArea);


            likeCountArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(askDytilaMain.this);
                    dialog.setContentView(R.layout.ask_dytila_likes_layout);

                    TextView closeText=(TextView)dialog.findViewById(R.id.close);
                    noLikesLayout=(RelativeLayout)dialog.findViewById(R.id.noLikesLayout);
                    loadingLayout=(RelativeLayout)dialog.findViewById(R.id.loadingLayout);
                    likesListView=(ListView)dialog.findViewById(R.id.likesList);
                    noLikesLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.VISIBLE);
                    likesListView.setVisibility(View.GONE);

                    getLikes(askDytila_allModelList.get(position).getAns_id());

                    closeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

            final int flag=0;

            final ImageView imageView=(ImageView)convertView.findViewById(R.id.likeImg);

            if(askDytila_allModelList.get(position).getLiked().equals("1"))
                imageView.setImageResource(R.drawable.like);
            else
                imageView.setImageResource(R.drawable.unlike);

            likeArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url="https://dytila.herokuapp.com/api/askDytila/like";
                    RequestQueue requestQueue=new Volley().newRequestQueue(askDytilaMain.this);
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        String status=jsonObject.getString("status");
                                        if(status.equals("dislike")){
                                            if((Integer.parseInt(likesCountText.getText().toString()))>=0){
                                                likesCountText.setText(""+((Integer.parseInt(likesCountText.getText().toString()))-1));
                                                imageView.setImageResource(R.drawable.unlike);
                                                askDytila_allModelList.get(position).setLiked("0");
                                                askDytila_allModelList.get(position).setLikesCount(""+(Integer.parseInt(likesCountText.getText().toString())));
                                            }
                                        }else if(status.equals("like")){
                                            likesCountText.setText(""+((Integer.parseInt(likesCountText.getText().toString()))+1));
                                            imageView.setImageResource(R.drawable.like);
                                            askDytila_allModelList.get(position).setLiked("1");
                                            askDytila_allModelList.get(position).setLikesCount("" + (Integer.parseInt(likesCountText.getText().toString())));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params=new HashMap<String,String>();
                            params.put("user_id",user_id);
                            params.put("ans_id",askDytila_allModelList.get(position).getAns_id());
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            });


            //Answer particular Question

            answerLayout=(RelativeLayout)convertView.findViewById(R.id.answerLayout);
            final View finalConvertView = convertView;
            answerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(askDytilaMain.this);
                    dialog.setContentView(R.layout.layout_ask_dytila_answer);
                    Button postAnswerButton,closeButton;
                    postAnswerButton=(Button) dialog.findViewById(R.id.postButton);
                    closeButton=(Button) dialog.findViewById(R.id.close);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    postAnswerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText editText=(EditText)dialog.findViewById(R.id.answer);
                            if(editText.getText().toString().length()<1){
                                Toast.makeText(getApplicationContext(),"Please write something!",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                dialog.dismiss();
                                postAnswer(user_id, username, askDytila_allModelList.get(position).getQue_id(), editText.getText().toString());
                            }
                        }
                    });
                    dialog.show();
                }
            });

            return convertView;
        }
    }
    public void getLikes(final String ans_id){
        String url="https://dytila.herokuapp.com/api/askDytila/answer/likes";
        RequestQueue requestQueue=new Volley().newRequestQueue(askDytilaMain.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingLayout.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            List<Likesmodel> likesModelList=new ArrayList<>();
                            JSONArray parentArray=jsonObject.getJSONArray("likes");
                            if(parentArray.length()<1){
                                likesListView.setVisibility(View.GONE);
                                noLikesLayout.setVisibility(View.VISIBLE);
                            }
                            else{
                                likesListView.setVisibility(View.VISIBLE);
                                noLikesLayout.setVisibility(View.GONE);
                                for(int i=0;i<parentArray.length();i++){
                                    JSONObject finalObject=parentArray.getJSONObject(i);
                                    Likesmodel likesmodel=new Likesmodel();
                                    likesmodel.setUsername(finalObject.getString("username"));
                                    likesmodel.setAvatar(finalObject.getString("avatar"));
                                    likesModelList.add(likesmodel);
                                }
                                postExecuteLikes(likesModelList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something wrong happend :( Please Try Again!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("ans_id",ans_id);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void postExecuteLikes(List<Likesmodel> result){
        LikesModelAdapter likesmodelAdapter=new LikesModelAdapter(getApplicationContext().getApplicationContext(),R.layout.row_likes,result);
        likesListView.setAdapter(likesmodelAdapter);
    }
    class LikesModelAdapter extends ArrayAdapter {
        public List<Likesmodel> likesmodelList;
        private int resource;

        private LayoutInflater inflater;

        public LikesModelAdapter(Context context, int resource, List<Likesmodel> objects) {
            super(context, resource, objects);
            likesmodelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_likes, null);
            }

            TextView username;
            ImageView img;
            username=(TextView)convertView.findViewById(R.id.usernameText);
            username.setText(upperFirst(likesmodelList.get(position).getUsername()));

            img=(ImageView)convertView.findViewById(R.id.img);
            ImageLoader.getInstance().displayImage(likesmodelList.get(position).getAvatar(), img);

            return convertView;
        }

    }
    public void postAnswer(final String user_id, final String username, final String que_id, final String ans_val){
        String url="https://dytila.herokuapp.com/api/askDytila/ans";
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Posting your Answer");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(askDytilaMain.this);
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
                Toast.makeText(getApplicationContext(),"Something Went Wrong! Please try again",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("user_id",user_id);
                params.put("username",username);
                params.put("que_id",que_id);
                params.put("ans",ans_val);
                return params;
            }
        };
        requestQueue.add(stringRequest);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCode) {
            if(resultCode == RESULT_OK) {
                String p=data.getStringExtra("selected");
                String isLiked=data.getStringExtra("isLiked");
                int pos=Integer.parseInt(p);
                View v=all.getChildAt(pos-all.getFirstVisiblePosition());
                if(v!=null){
                    if(isLiked.equals("0")){
                        ImageView likeImg=(ImageView)v.findViewById(R.id.likeImg);
                        likeImg.setImageResource(R.drawable.unlike);
                        TextView likesCount=(TextView)v.findViewById(R.id.likesCount);
                        likesCount.setText(""+((Integer.parseInt(likesCount.getText().toString()))-1));
                    }else if(isLiked.equals("1")){
                        ImageView likeImg=(ImageView)v.findViewById(R.id.likeImg);
                        likeImg.setImageResource(R.drawable.like);
                        TextView likesCount=(TextView)v.findViewById(R.id.likesCount);
                        likesCount.setText("" + ((Integer.parseInt(likesCount.getText().toString())) + 1));
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Something wrong happened :( Please Try Again!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

