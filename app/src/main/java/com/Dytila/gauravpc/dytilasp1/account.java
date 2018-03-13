package com.Dytila.gauravpc.dytilasp1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class account extends AppCompatActivity {

    TextView user_name,user_email,user_mobile,user_ID;
    ImageView cross_goBack,changeAddressImg;
    RelativeLayout accountDetails;
    CardView view3_Card,info1,info2,info3,info4,info5,info6,order_id_not_avail_Card,changeLocationCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountDetails=(RelativeLayout)findViewById(R.id.account_details);
        view3_Card=(CardView)findViewById(R.id.view3);

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String userid_val=sharedPreferences.getString("user_id", "");
        String username_val=sharedPreferences.getString("username", "");
        String mobile_val=sharedPreferences.getString("mobile", "");
        String email_val=sharedPreferences.getString("email","");
        String orderid_val=sharedPreferences.getString("order_id", "");
        String avatar_val=sharedPreferences.getString("avatar","");
        String mealName_val=sharedPreferences.getString("mealName", "");
        String location_val=sharedPreferences.getString("location","");
        String TmealFreq_val=sharedPreferences.getString("TmealFreq","");
        String mealFreq_val=sharedPreferences.getString("mealFreq","");
        String timing_val=sharedPreferences.getString("timing","");
        String specs_val=sharedPreferences.getString("specs", "");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        ImageView avatar=(ImageView)findViewById(R.id.userImg);
        ImageLoader.getInstance().displayImage(avatar_val, avatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(account.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.avatar_option_layout);

                RelativeLayout removeImage,changeImage;
                removeImage=(RelativeLayout)dialog.findViewById(R.id.remove);
                changeImage=(RelativeLayout)dialog.findViewById(R.id.change);

                changeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(account.this, changeAvatar.class);
                        intent.putExtra("operation", "changeImg");
                        startActivity(intent);
                        finish();
                    }
                });

                removeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(account.this, changeAvatar.class);
                        intent.putExtra("operation", "removeImg");
                        startActivity(intent);
                        finish();
                    }
                });

                TextView close=(TextView)dialog.findViewById(R.id.closeText);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        user_name=(TextView)findViewById(R.id.name);
        user_email=(TextView)findViewById(R.id.email);
        user_mobile=(TextView)findViewById(R.id.mobile);
        user_ID=(TextView)findViewById(R.id.user_id);

        user_name.setText(upperFirst(username_val));
        user_email.setText(email_val);
        user_mobile.setText(mobile_val);
        user_ID.setText(userid_val);



        info2=(CardView)findViewById(R.id.info2);
        info3=(CardView)findViewById(R.id.info3);
        info4=(CardView)findViewById(R.id.info4);
        info5=(CardView)findViewById(R.id.info5); //Dytila Kitchen card
        info6=(CardView)findViewById(R.id.info6); //lgout card

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(account.this,change_password.class);
                startActivity(intent);
            }
        });
        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(account.this,profile_settings.class);
                intent.putExtra("setting","Change Email");
                startActivity(intent);
            }
        });
        info4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(account.this,profile_settings.class);
                intent.putExtra("setting","Change Mobile");
                startActivity(intent);
            }
        });
        info5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("userInfo",Context.MODE_APPEND);
                String mKitchen=sharedPreferences.getString("mKitchen","");
                if(mKitchen.equals("null")){
                    final Dialog dialog = new Dialog(account.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.can_order_layout);
                    TextView t1Text=(TextView)dialog.findViewById(R.id.t1);
                    t1Text.setText("Oops! o_O");
                    TextView t2Text=(TextView)dialog.findViewById(R.id.t2);
                    t2Text.setText("Please let us find a kitchen near you!");
                    TextView t3Text=(TextView)dialog.findViewById(R.id.t3);
                    t3Text.setText("Go to home and click on 'My Dytila Kitchen' and then find again :)");
                    TextView close = (TextView) dialog.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(mKitchen.equals("unavailable")){
                    final Dialog dialog = new Dialog(account.this);
                    dialog.setContentView(R.layout.can_order_layout);
                    TextView close = (TextView) dialog.findViewById(R.id.close);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else{
                    boolean check=isNetworkAvailable();
                    if(check==true) {
                        String[] val=mKitchen.split(":");

                        String add=val[2];
                        String mobile=val[3];

                        String full_address_of_kitchen="Address:\n"+add;

                        final Dialog dialog = new Dialog(account.this);
                        dialog.setContentView(R.layout.cannot_order_layout);

                        TextView textView=(TextView) dialog.findViewById(R.id.t2);
                        textView.setText("Dytila Kitchen near you");

                        TextView close = (TextView) dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        TextView addressText=(TextView) dialog.findViewById(R.id.address);
                        addressText.setText(full_address_of_kitchen);

                        TextView mobileText=(TextView)dialog.findViewById(R.id.mobile);
                        mobileText.setText(mobile);
                        dialog.show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        info6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent=new Intent(account.this,activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        //Delivery Addreess

        cross_goBack=(ImageView)findViewById(R.id.cross);
        cross_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(account.this,features.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        Intent intent=new Intent(account.this,features.class);
        startActivity(intent);
        finish();
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