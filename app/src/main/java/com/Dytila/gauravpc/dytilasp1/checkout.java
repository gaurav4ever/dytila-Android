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
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class checkout extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    String userIDValue;
    TextView user_name,user_email;

    Button couponButton,addon_button,order_button;
    TextView mealNametext,programNameText,madeByText,priceText,countItemText,couponStatusText;
    EditText couponEditText,pickupAddressText,addon_text;
    ImageView minusImg,addImg,food_img,userAvatar;
    CheckBox monthlyPlanCheckBox;
    RelativeLayout monthlyPlanAreaLayout,addonBodyLayout;
    Spinner mealFreqSpinner,timingSpinner;
    int flag=0,discount_val=0,isCouponApplyButtonPressed=0,couponFlag=0,addonFlag=0,ifIssetOrderButton=0;

    String couponCode;

    private ProgressDialog pDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        final String userid_val=sharedPreferences.getString("user_id", "");
        String mobile_val=sharedPreferences.getString("mobile","");
        final String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String mealFreq_val=sharedPreferences.getString("mealFreq","");
        String location_val=sharedPreferences.getString("location","");
        String avatar_val=sharedPreferences.getString("avatar","");
        Log.e("values", userid_val);
        //Shared Preferences Ends
        userIDValue=userid_val; //for menu item

        //Navigation Operation Begins

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(checkout.this,account.class);
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
                Intent intent=new Intent(checkout.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(checkout.this,account.class);
                startActivity(intent);
                finish();
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, Order_details.class);
                startActivity(intent);
                finish();
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkout.this, bills.class);
                startActivity(intent);
                finish();
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(checkout.this,renew.class);
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
                Intent intent = new Intent(checkout.this, activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(checkout.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

        //main Content Code

        Intent intent=getIntent();
        final String food_id_val=intent.getStringExtra("id");
        final String mealName_val=intent.getStringExtra("mealName");
        final String mealProgram_val=intent.getStringExtra("programName");
        final String food_type_val=intent.getStringExtra("food_type");
        final String type_Val=intent.getStringExtra("type");
        final String foodImg_Val=intent.getStringExtra("food_img");
        final String price_val=intent.getStringExtra("price");
        mealNametext=(TextView)findViewById(R.id.mealName);
        mealNametext.setText(upperFirst(mealName_val));

        food_img=(ImageView)findViewById(R.id.foodImg);
        ImageLoader.getInstance().displayImage(foodImg_Val, food_img);

        priceText=(TextView)findViewById(R.id.price);
        priceText.setText(price_val);
        final String priceTemp=priceText.getText().toString();
        final String[] priceValue = {priceTemp.substring(2)};
        final int[] priceValueInt = {Integer.parseInt(priceValue[0])};
        final int[] finalPriceValue = new int[1];
        finalPriceValue[0]= priceValueInt[0];
        Log.e("price value", priceValue[0]);

        minusImg=(ImageView)findViewById(R.id.minus);
        addImg=(ImageView)findViewById(R.id.add);


        minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countItemText=(TextView)findViewById(R.id.countItem);
                priceText=(TextView)findViewById(R.id.price);

                String val=countItemText.getText().toString();

                if(!val.equals("1")){
                    int new_val=Integer.parseInt(val);
                    new_val=new_val-1;
                    countItemText.setText(String.valueOf(Integer.parseInt(val)-1));
                    finalPriceValue[0] = priceValueInt[0] *new_val;

                    if(discount_val!=0){
                        int d=discount_val;
                        float _d=((float)d)/100;
                        float p= (finalPriceValue[0] - (finalPriceValue[0] * _d));
                        int pFinal=Math.round(p);
                        priceText.setText("₹ "+ pFinal);
                    }
                    else{
                        priceText.setText("₹ "+ finalPriceValue[0]);
                    }
                }
            }
        });
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countItemText = (TextView) findViewById(R.id.countItem);
                priceText=(TextView)findViewById(R.id.price);

                String val = countItemText.getText().toString();

                int new_val = Integer.parseInt(val);
                new_val=new_val+1;
                countItemText.setText(String.valueOf(Integer.parseInt(val)+1));
                finalPriceValue[0]= priceValueInt[0] *new_val;

                if(discount_val!=0){
                    int d=discount_val;
                    float _d=((float)d)/100;
                    float p= (finalPriceValue[0] - (finalPriceValue[0] * _d));
                    int pFinal=Math.round(p);
                    priceText.setText("₹ "+ pFinal);
                }
                else{
                    priceText.setText("₹ "+ finalPriceValue[0]);
                }
            }
        });


        RelativeLayout setTimeLayout=(RelativeLayout)findViewById(R.id.setTimeLayout);
        final TextView timeText=(TextView)findViewById(R.id.time);
        setTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(checkout.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_date_picker);

                TextView setTimeTextView=(TextView)dialog.findViewById(R.id.timeTextView);
                setTimeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Date dt = new Date();
                        Calendar cal = Calendar.getInstance();
                        int now_hour=cal.get(Calendar.HOUR_OF_DAY);
                        int now_min=cal.get(Calendar.MINUTE);

                        TimePicker timePicker=(TimePicker)dialog.findViewById(R.id.timePicker);
                        int hour = 0;
                        int min = 0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                            min = timePicker.getMinute();

                            String AM_PM ;
                            if(hour < 12) {
                                AM_PM = "AM";
                                timeText.setText(""+(hour)+":"+min+AM_PM);
                            } else {
                                AM_PM = "PM";
                                timeText.setText(""+(hour-12)+":"+min+AM_PM);
                            }

//                            Log.e("current hour", now_hour + "");

//                                timeText.setText(""+(hour-12)+":"+min+AM_PM);
                                dialog.dismiss();
                            }
                    }
                });
                dialog.show();
            }
        });

        //coupon operation
        final RelativeLayout couponBodyLayout=(RelativeLayout)findViewById(R.id.couponBody);
        couponBodyLayout.setVisibility(View.GONE);

        CardView info2_card=(CardView)findViewById(R.id.info3);

        info2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(couponFlag==0){
                    couponBodyLayout.setVisibility(View.VISIBLE);
                    ImageView moreorlessImg=(ImageView)findViewById(R.id.moreorless);
                    moreorlessImg.setImageResource(R.drawable.minus);
                    couponFlag=1;
                }
                else if(couponFlag==1){
                    couponBodyLayout.setVisibility(View.GONE);
                    ImageView moreorlessImg=(ImageView)findViewById(R.id.moreorless);
                    moreorlessImg.setImageResource(R.drawable.add);
                    couponFlag=0;
                }

            }
        });
        couponButton=(Button)findViewById(R.id.couponApplyButton);
        couponStatusText=(TextView)findViewById(R.id.couponStatus);
        couponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponEditText=(EditText)findViewById(R.id.couponText);

                //checking for space in coupon code
                Pattern pattern = Pattern.compile("\\s");
                Matcher matcher = pattern.matcher(couponEditText.getText().toString());
                boolean found = matcher.find();

                if(couponEditText.getText().toString().length()<1){
                    TextView discountStatus=(TextView)findViewById(R.id.couponStatus);
                    discountStatus.setText("Please enter coupen code");
                }
                else if(found){
                    TextView discountStatus=(TextView)findViewById(R.id.couponStatus);
                    discountStatus.setText("O_o  Invalid Coupon  o_O");
                }
                else{
                    couponCode=couponEditText.getText().toString();
                    Log.e("coupon Code",couponCode);
                    boolean check=isNetworkAvailable();
                    if(check==true) {
                        String url="https://dytila.herokuapp.com/api/coupons/"+userid_val+","+couponCode;
                        Log.e("values",url);
                        new JSONTask().execute(url);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




        pickupAddressText=(EditText)findViewById(R.id.pickupAddress);
        if(location_val.equals("Not Specified")){
            pickupAddressText.setHint("Please Specify an Address");
        }
        else{
            pickupAddressText.setText(location_val);
        }

//        //monthly plan meal pack operations
//        monthlyPlanCheckBox=(CheckBox)findViewById(R.id.monthlyPlanCheckBox);
//        monthlyPlanAreaLayout=(RelativeLayout)findViewById(R.id.monthlyPlanArea);
//        monthlyPlanAreaLayout.setVisibility(View.GONE);
//        monthlyPlanCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(monthlyPlanCheckBox.isChecked()){
//                    monthlyPlanAreaLayout.setVisibility(View.VISIBLE);
//                    flag=1;
//                }
//                else{
//                    monthlyPlanAreaLayout.setVisibility(View.GONE);
//                    flag=0;
//                }
//            }
//        });
//
//        mealFreqSpinner=(Spinner)findViewById(R.id.mealFreqSpinner);
//        timingSpinner=(Spinner)findViewById(R.id.mealTimingSpinner);
//
//        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.mealFreqOptions,android.R.layout.simple_spinner_item);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mealFreqSpinner.setAdapter(adapter1);
//
//        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.timingOptions,android.R.layout.simple_spinner_item);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        timingSpinner.setAdapter(adapter2);
//
//        mealFreqSpinner.setOnItemSelectedListener(this);
//        timingSpinner.setOnItemSelectedListener(this);

        final String[] addonText_val = new String[1];
        addonText_val[0]="No addons";

        addonBodyLayout=(RelativeLayout)findViewById(R.id.addonBody);
        addonBodyLayout.setVisibility(View.GONE);

        CardView info6_card=(CardView)findViewById(R.id.info6);
        info6_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addonFlag==0){
                    addonBodyLayout.setVisibility(View.VISIBLE);
                    ImageView moreorlessImg2=(ImageView)findViewById(R.id.moreorless2);
                    moreorlessImg2.setImageResource(R.drawable.minus);
                    addonFlag=1;
                }
                else if(addonFlag==1){
                    addonBodyLayout.setVisibility(View.GONE);
                    ImageView moreorlessImg2=(ImageView)findViewById(R.id.moreorless2);
                    moreorlessImg2.setImageResource(R.drawable.add);
                    addonFlag=0;
                }

            }
        });


        addon_button=(Button)findViewById(R.id.addonButton);
        addon_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addon_text=(EditText)findViewById(R.id.addonText);
                if(addon_text.getText().toString().length()<1){
                    Toast.makeText(getApplicationContext(),"Addons Empty!",Toast.LENGTH_SHORT).show();
                }
                else{
                    addonText_val[0] = addon_text.getText().toString();
                    Toast.makeText(getApplicationContext(),"Addons Added",Toast.LENGTH_SHORT).show();
                }
            }
        });

        order_button=(Button)findViewById(R.id.orderButton);
        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView timeTextView=(TextView)findViewById(R.id.time);
                ifIssetOrderButton=1;
                String  mealName=mealName_val,
                        programName=mealProgram_val,

                        itemCount=(countItemText=(TextView)findViewById(R.id.countItem)).getText().toString(),

                        priceValue=((priceText=(TextView)findViewById(R.id.price)).getText().toString()).substring(2),

                        pickUpAddress="",

                        time=timeTextView.getText().toString();
                pickupAddressText=(EditText)findViewById(R.id.pickupAddress);
                if(pickupAddressText.getText().toString().length()<1){
                    Toast.makeText(getApplicationContext(),"Please Specify Delivery Address!",Toast.LENGTH_SHORT).show();
                }
                else if(time.equals("Select Time")){
                    Toast.makeText(getApplicationContext(),"Please Specify Time",Toast.LENGTH_SHORT).show();
                }
                else{
                    pickUpAddress=pickupAddressText.getText().toString();

//                    Add pickUpAddress to the location value in shared preferences
                    SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("location",pickUpAddress);
                    editor.commit();

                    String addons="null";
                    addons=addonText_val[0];


                    if(isCouponApplyButtonPressed==1){

                        boolean check=isNetworkAvailable();
                        if(check==true) {
                            String food_id=food_id_val;
                            Log.e("food_id",food_id);
                            String food_type=food_type_val;
                            String vals=userid_val+","+food_id+","+food_type+","+itemCount+","+priceValue+","+pickUpAddress+","+addons+","+couponCode+","+time;
                            String url="https://dytila.herokuapp.com/api/regular_user_order/"+vals;
                            Log.e("values",url);
                            postData(userid_val, food_id, food_type, itemCount, priceValue, pickUpAddress, addons, couponCode,time);

                            Toast.makeText(getApplicationContext(),"Order Placed Successfully!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        couponCode="null";
                        boolean check=isNetworkAvailable();
                        if(check==true) {
                            Log.e("coupon code",couponCode);
                            String food_id=food_id_val;
                            String food_type=food_type_val;
                            String vals=userid_val+","+food_id+","+food_type+","+itemCount+","+priceValue+","+pickUpAddress+","+addons+","+"null";
                            String url="https://dytila.herokuapp.com/api/regular_user_order/"+vals;
                            Log.e("values", url);
                            postData(userid_val,food_id,food_type,itemCount,priceValue,pickUpAddress,addons,couponCode,time);
                        }
                        else{
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

    //coupon operation jsonTask
    public class JSONTask extends AsyncTask<String,String ,String> {

        private ProgressDialog dialog = new ProgressDialog(checkout.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setTitle("Applying Coupon...");
            this.dialog.setMessage("Please Wait ...");
            this.dialog.show();
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
                        dialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(checkout.this);
                        alertDialogBuilder.setMessage("Connection Is Unstable!");
                        alertDialogBuilder.setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(checkout.this, checkout.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(checkout.this, features.class);
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
        protected String doInBackground(String... params) {
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

                String discount=parentObject.getString("discount");
                String status=parentObject.getString("status");

                return discount+","+status;

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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String value[]=result.split(",");
            String discount,status;
            discount=value[0];
            status=value[1];

            //for discount
            TextView discountStatus=(TextView)findViewById(R.id.couponStatus);
            if(status.equals("success")){

                isCouponApplyButtonPressed=1;
                TextView priceTextValue=(TextView)findViewById(R.id.price);
                int d=Integer.parseInt(discount);
                Log.e("discount",""+d);
                discount_val=d;
                float d_=((float)d)/100;
                Log.e("d_1",""+d_);
                String priceTemp=priceTextValue.getText().toString().substring(2);
                Log.e("price Temp Value",priceTemp);
                int old_price=Integer.parseInt(priceTemp);
                Log.e("Old Value",""+old_price);
                Log.e("d_2",""+d_);
                float final_price= (old_price - (old_price * d_));
                int fp = Math.round(final_price);
                Log.e("final price Value",""+fp);
                priceTextValue.setText("₹ "+fp);

                discountStatus.setText("Coupon Applied!\n\n"+discount+"% discount :D");
            }
            else if(status.equals("expired")) {
                couponCode="null";

                TextView couponTextAfterButtonClicked=(EditText)findViewById(R.id.couponText);
                couponTextAfterButtonClicked.setText("");


                discountStatus.setText("):  Coupon Expired  :(");
            }
            else {
                couponCode="null";

                TextView couponTextAfterButtonClicked=(EditText)findViewById(R.id.couponText);
                couponTextAfterButtonClicked.setText("");
                discountStatus.setText("O_o  Invalid Coupon  o_O");
            }


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


    public void postData(final String param_userid_val,final String param_food_id,final String param_food_type,final String param_itemCount,final String param_priceValue,final String param_pickUpAddress,final String param_addons,final String param_couponCode, final String param_time){
        String url="https://dytila.herokuapp.com/api/regular_user_order";
        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(checkout.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        couponCode="null";

                        TextView couponTextAfterButtonClicked=(EditText)findViewById(R.id.couponText);
                        couponTextAfterButtonClicked.setText("");

                        //print layout in success
                        final Dialog dialog = new Dialog(checkout.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.layout_thankyou);
                        TextView t1Text=(TextView)dialog.findViewById(R.id.t1);
                        t1Text.setText("Order placed successfully \n Thank You");
                        TextView close=(TextView)dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent=new Intent(checkout.this,Order_details.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        dialog.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong! Please try again.",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("user_id",param_userid_val);
                params.put("food_id",param_food_id);
                params.put("food_type",param_food_type);
                params.put("item_count",param_itemCount);
                params.put("price",param_priceValue);
                params.put("pick_up_address",param_pickUpAddress);
                params.put("addons",param_addons);
                params.put("coupon_code",param_couponCode);
                params.put("time",param_time);
                return params;
            }
        };
        requestQueue.add(stringRequest);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(ifIssetOrderButton==1){
                Intent intent=new Intent(checkout.this,features.class);
                startActivity(intent);
                finish();
            }
            super.onBackPressed();
        }
    }
}
