package com.Dytila.gauravpc.dytilasp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.Dytila.gauravpc.dytilasp1.CustomMealsHandler.customMealPlanDetail;
import com.Dytila.gauravpc.dytilasp1.models.CustomMealPlansModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class custom_meal_plans extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView user_name,user_email,noLikesText;
    Toolbar toolbar;
    ImageView userAvatar,readImg,askImg,ansImg,tagsImg,refreshImg;
    private ListView cmpListView;
    ListView likesListView;
    RelativeLayout r,no_connection_layout,noLikesLayout,loadingLayout,answerLayout;
    int asyncTaskDone=0;
    String user_id,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_meal_plans);

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
                Intent intent=new Intent(custom_meal_plans.this,account.class);
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
                Intent intent=new Intent(custom_meal_plans.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(custom_meal_plans.this,account.class);
                startActivity(intent);
                finish();
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(custom_meal_plans.this, Order_details.class);
                startActivity(intent);
                finish();
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(custom_meal_plans.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(custom_meal_plans.this, bills.class);
                startActivity(intent);
                finish();
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(custom_meal_plans.this,renew.class);
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
                Intent intent = new Intent(custom_meal_plans.this, activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(custom_meal_plans.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

        cmpListView = (ListView) findViewById(R.id.cmp_listView);
        cmpListView.setVisibility(View.GONE);
        r=(RelativeLayout)findViewById(R.id.loadingLayout);
        r.setVisibility(View.VISIBLE);
        no_connection_layout=(RelativeLayout)findViewById(R.id.no_connectionLayout);
        no_connection_layout.setVisibility(View.GONE);

        boolean check=isNetworkAvailable();
        if(check==true) {
            findCustomMealPlans();
        }
        else{
            no_connection_layout.setVisibility(View.VISIBLE);
            r.setVisibility(View.GONE);
            Button retry_button=(Button)findViewById(R.id.retryButton);
            retry_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(custom_meal_plans.this, custom_meal_plans.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void findCustomMealPlans(){
        String url="https://dytila.herokuapp.com/api/dytilaMainMenu/custom_meal_plans";
        r.setVisibility(View.VISIBLE);
        RequestQueue requestQueue=new Volley().newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<CustomMealPlansModel> customMealPlansModelList=new ArrayList<>();
                    JSONArray parentArray=response.getJSONArray("custom_meal_plans");

                        for(int i=0;i<parentArray.length();i++){
                            JSONObject finalObject=parentArray.getJSONObject(i);
                            CustomMealPlansModel customMealPlansModel=new CustomMealPlansModel();
                            customMealPlansModel.setPlan_id(finalObject.getString("plan_id"));
                            customMealPlansModel.setPlanName(finalObject.getString("program"));
                            customMealPlansModel.setDuration(finalObject.getString("duration"));
                            customMealPlansModel.setCost(finalObject.getString("cost"));
                            customMealPlansModel.setDay1(finalObject.getString("day1"));
                            customMealPlansModel.setDay2(finalObject.getString("day2"));
                            customMealPlansModel.setDay3(finalObject.getString("day3"));
                            customMealPlansModel.setDay4(finalObject.getString("day4"));
                            customMealPlansModel.setDay5(finalObject.getString("day5"));
                            customMealPlansModel.setDay6(finalObject.getString("day6"));
                            customMealPlansModel.setDay7(finalObject.getString("day7"));
                            customMealPlansModelList.add(customMealPlansModel);
                        }
                        postExecute(customMealPlansModelList);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Some Thing Went Wrong! Please Try Again!", Toast.LENGTH_SHORT).show();
                }
                cmpListView.setVisibility(View.VISIBLE);
                r.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext().getApplicationContext(),"Some Thing Went Wrong! Please Try Again!",Toast.LENGTH_SHORT).show();
                cmpListView.setVisibility(View.VISIBLE);
                r.setVisibility(View.GONE);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void postExecute(List<CustomMealPlansModel> result){

        CustomMealPlansModelAdapter customMealPlansModelAdapter=new CustomMealPlansModelAdapter(getApplicationContext().getApplicationContext(),R.layout.row_custom_meal_plans,result);
        cmpListView.setAdapter(customMealPlansModelAdapter);
    }
    class CustomMealPlansModelAdapter extends ArrayAdapter {
        public List<CustomMealPlansModel> customMealPlansModelList;
        private int resource;

        private LayoutInflater inflater;

        public CustomMealPlansModelAdapter(Context context, int resource, List<CustomMealPlansModel> objects) {
            super(context, resource, objects);
            customMealPlansModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_custom_meal_plans, null);
            }

            TextView mealPlanNameTextView,PriceTextView;
            mealPlanNameTextView=(TextView)convertView.findViewById(R.id.meal_plan_name);
            PriceTextView=(TextView)convertView.findViewById(R.id.price);
            mealPlanNameTextView.setText(customMealPlansModelList.get(position).getPlanName());
            PriceTextView.setText(customMealPlansModelList.get(position).getCost());

            CardView mealPlanCardView=(CardView)convertView.findViewById(R.id.mealPlanCard);
            mealPlanCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(custom_meal_plans.this,customMealPlanDetail.class);
                    intent.putExtra("plan_id",customMealPlansModelList.get(position).getPlan_id());
                    intent.putExtra("mealPlanName",customMealPlansModelList.get(position).getPlanName());
                    intent.putExtra("price",customMealPlansModelList.get(position).getCost());
                    intent.putExtra("duration",customMealPlansModelList.get(position).getDuration());
                    intent.putExtra("day1",customMealPlansModelList.get(position).getDay1());
                    intent.putExtra("day2",customMealPlansModelList.get(position).getDay2());
                    intent.putExtra("day3",customMealPlansModelList.get(position).getDay3());
                    intent.putExtra("day4",customMealPlansModelList.get(position).getDay4());
                    intent.putExtra("day5",customMealPlansModelList.get(position).getDay5());
                    intent.putExtra("day6",customMealPlansModelList.get(position).getDay6());
                    intent.putExtra("day7",customMealPlansModelList.get(position).getDay7());
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
