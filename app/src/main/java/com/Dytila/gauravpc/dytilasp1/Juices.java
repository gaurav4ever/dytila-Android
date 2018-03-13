package com.Dytila.gauravpc.dytilasp1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Dytila.gauravpc.dytilasp1.models.mealModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

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

public class Juices extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    private ListView juicesList;
    TextView user_name,user_email;
    ImageView userAvatar;
    GridView gridView;
    RelativeLayout r,no_connection_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        String userid_val=sharedPreferences.getString("user_id","");
        String mobile_val=sharedPreferences.getString("mobile","");
        String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String avatar_val=sharedPreferences.getString("avatar","");
        //Shared Preferences Ends

        //Navigation Operation Begins

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Juices.this,account.class);
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
                Intent intent=new Intent(Juices.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Juices.this,account.class);
                startActivity(intent);
                finish();
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Juices.this, Order_details.class);
                startActivity(intent);
                finish();
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Juices.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Juices.this, bills.class);
                startActivity(intent);
                finish();
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Juices.this,renew.class);
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(Juices.this, activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Juices.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends



        ImageView headerImage1=(ImageView)findViewById(R.id.headerImage1);
        ImageView headerImage2=(ImageView)findViewById(R.id.headerImage2);

        MemoryCacheUtils.removeFromCache("https://dytila.herokuapp.com/static/img/app%20pics/juices_back1.png", ImageLoader.getInstance().getMemoryCache());
        DiskCacheUtils.removeFromCache("https://dytila.herokuapp.com/static/img/app%20pics/juices_back1.png", ImageLoader.getInstance().getDiskCache());

        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/juices_back2.png", headerImage2);
        ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/juices_back1.png", headerImage1);

        MemoryCacheUtils.removeFromCache("https://dytila.herokuapp.com/static/img/app%20pics/juices_back2.png", ImageLoader.getInstance().getMemoryCache());
        DiskCacheUtils.removeFromCache("https://dytila.herokuapp.com/static/img/app%20pics/juices_back2.png", ImageLoader.getInstance().getDiskCache());

        gridView=(GridView)findViewById(R.id.grid);
        gridView.setVisibility(View.GONE);
        r=(RelativeLayout)findViewById(R.id.loadingLayout);
        r.setVisibility(View.VISIBLE);
        no_connection_layout=(RelativeLayout)findViewById(R.id.no_connectionLayout);
        no_connection_layout.setVisibility(View.GONE);

        boolean check=isNetworkAvailable();
        if(check==true) {
            new JSONTask().execute("https://dytila.herokuapp.com/api/dytilaMainMenu/juices/"+userid_val);
        }
        else{
            no_connection_layout.setVisibility(View.VISIBLE);
            r.setVisibility(View.GONE);
            Button retry_button=(Button)findViewById(R.id.retryButton);
            retry_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Juices.this,Juices.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        ImageView goBackImage=(ImageView)findViewById(R.id.goBackImage);
        goBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("menu option", "Menu Made");
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("menu option", "Menu item now");
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    public class JSONTask extends AsyncTask<String, String, List<mealModel>> {


        @Override
        protected void onPreExecute() {

            r.setVisibility(View.VISIBLE);
            //timeout for Async Task
            final JSONTask asyncObject = this;
            new CountDownTimer(10000, 10000) {
                public void onTick(long millisUntilFinished) {
                    // monitor the progress
                }
                public void onFinish() {
                    // stop async task if not in progress
                    if (asyncObject.getStatus() == Status.RUNNING) {
                        asyncObject.cancel(false);
                        r.setVisibility(View.GONE);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Juices.this);
                        alertDialogBuilder.setMessage("Connection Is Unstable!");
                        alertDialogBuilder.setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(Juices.this, Juices.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(Juices.this, features.class);
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
        protected List<mealModel> doInBackground(String... params) {
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

                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("juices");

                List<mealModel> mealModelList = new ArrayList<>();
                for (int i = 0; i < parentArray.length(); i++) {

                    JSONObject finalObject = parentArray.getJSONObject(i);
                    mealModel mealModel = new mealModel();
                    mealModel.setId(finalObject.getString("id"));
                    mealModel.setName(finalObject.getString("name"));
                    mealModel.setProgram(finalObject.getString("programs"));
                    mealModel.setType(finalObject.getString("type"));
                    mealModel.setImg(finalObject.getString("img"));
                    mealModel.setProtein(finalObject.getString("protein"));
                    mealModel.setCarbs(finalObject.getString("carbs"));
                    mealModel.setFats(finalObject.getString("fats"));
                    mealModel.setCalories(finalObject.getString("calories"));
                    mealModel.setPrice(finalObject.getString("price"));
                    mealModel.setDescription(finalObject.getString("description"));
                    mealModel.setItems_included(finalObject.getString("items_included"));
                    mealModel.setFi(finalObject.getString("fi"));
                    mealModel.setStatus(finalObject.getString("status"));
                    mealModel.setIsFav(finalObject.getString("isFav"));
                    mealModelList.add(mealModel);
                }

                return mealModelList;

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
        protected void onPostExecute(List<mealModel> mealModels) {
            super.onPostExecute(mealModels);

            gridView.setVisibility(View.VISIBLE);
            r.setVisibility(View.GONE);

            mealsAdapter mealsAdapter = new mealsAdapter(getApplicationContext(), R.layout.dytila_meal_row, mealModels);
            gridView.setAdapter(mealsAdapter);
        }
    }

    public class mealsAdapter extends ArrayAdapter {
        public List<mealModel> mealModelList;
        private int resource;
        private LayoutInflater inflater;

        public mealsAdapter(Context context, int resource, List<mealModel> objects) {
            super(context, resource,objects);
            mealModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.gridlayout_meal,null);
            }

            MemoryCacheUtils.removeFromCache(mealModelList.get(position).getImg(), ImageLoader.getInstance().getMemoryCache());
            DiskCacheUtils.removeFromCache(mealModelList.get(position).getImg(), ImageLoader.getInstance().getDiskCache());
//
//                ImageView comingSoonImg=(ImageView)convertView.findViewById(R.id.coming_soon_img);
//
            CardView view;
            view=(CardView)convertView.findViewById(R.id.view);
            TextView mealName,type,program,priceText;
            ImageView mealImg;
//                Button viewButton;
            mealImg=(ImageView)convertView.findViewById(R.id.mealImage);
            mealName=(TextView)convertView.findViewById(R.id.mealName);
//                type=(TextView)convertView.findViewById(R.id.type);
            program=(TextView)convertView.findViewById(R.id.program);
            priceText=(TextView)convertView.findViewById(R.id.price);
//
            mealName.setText(upperFirst(mealModelList.get(position).getName()));
//                type.setText(makeFirstUpper(mealModelList.get(position).getType()));
            program.setText(makeFirstUpper(mealModelList.get(position).getProgram() + " Program"));
            priceText.setText(mealModelList.get(position).getPrice());
//
            if(mealModelList.get(position).getStatus().equals("coming soon")){
                priceText.setText("Coming Soon");
            }

//
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Juices.this, mealHandler.class);
                    intent.putExtra("position", "position : " + position);
                    intent.putExtra("id", mealModelList.get(position).getId());
                    intent.putExtra("arrayVal", "juices");
                    intent.putExtra("url", "https://dytila.herokuapp.com/api/dytilaMainMenu/nonveg");
                    intent.putExtra("name", mealModelList.get(position).getName());
                    intent.putExtra("type", mealModelList.get(position).getType());
                    intent.putExtra("program", mealModelList.get(position).getProgram());
                    intent.putExtra("img", mealModelList.get(position).getImg());
                    intent.putExtra("protein", mealModelList.get(position).getProtein());
                    intent.putExtra("fats", mealModelList.get(position).getFats());
                    intent.putExtra("carbs", mealModelList.get(position).getCarbs());
                    intent.putExtra("calories", mealModelList.get(position).getCalories());
                    intent.putExtra("price", mealModelList.get(position).getPrice());
                    intent.putExtra("items_included", mealModelList.get(position).getItems_included());
                    intent.putExtra("fi",mealModelList.get(position).getFi());
                    intent.putExtra("description", mealModelList.get(position).getDescription());
                    intent.putExtra("status", mealModelList.get(position).getStatus());
                    intent.putExtra("isFav", mealModelList.get(position).getIsFav());
                    startActivity(intent);
                }
            });
            ImageLoader.getInstance().displayImage(mealModelList.get(position).getImg(), mealImg);
            mealModelList.get(position).setImg(mealModelList.get(position).getImg());

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
