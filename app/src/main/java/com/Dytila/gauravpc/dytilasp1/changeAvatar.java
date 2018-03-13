package com.Dytila.gauravpc.dytilasp1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class changeAvatar extends AppCompatActivity {

    ImageView imageView,chooseImg;
    TextView uploadText,cancelText;
    private Bitmap bitmap;
    int imageSet=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        final String userid_val=sharedPreferences.getString("user_id", "");
        final String username_val=sharedPreferences.getString("username", "");
        String avatar_val=sharedPreferences.getString("avatar","");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        imageView=(ImageView)findViewById(R.id.avatar);
        ImageLoader.getInstance().displayImage(avatar_val, imageView);

        Intent intent=getIntent();
        String operation=intent.getStringExtra("operation");
        if(operation.equals("changeImg")){
            chooseImg=(ImageView)findViewById(R.id.choose);
            uploadText=(TextView)findViewById(R.id.upload);
            cancelText=(TextView)findViewById(R.id.cancel);
            chooseImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplication(),"Please Wait. Opening Photos",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                }
            });

            uploadText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imageSet==1) {
                        uploadImage(userid_val, username_val);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please select avatar from gallery",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1=new Intent(changeAvatar.this,features.class);
                    startActivity(intent1);
                    finish();
                }
            });
        }
        else if(operation.equals("removeImg")){
            removeAvatar(userid_val);
        }
    }
    public String getStringImg(Bitmap bmp){
        int maxSize=300;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        bmp=Bitmap.createScaledBitmap(bmp,width,height,true);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] imageBytes=baos.toByteArray();
        String encodeImage= Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodeImage;
    }
    public void uploadImage(final String userid_val,final String username_val){
        final String url="https://dytila.herokuapp.com/api/upload_avatar";
        final ProgressDialog loading=ProgressDialog.show(this,"Uploading Avatar","please wait...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        String avatar_url = null;
                        //getting response from server
                        try {
                            JSONObject mResponse=new JSONObject(response);
                            avatar_url=mResponse.getString("avatar_url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        MemoryCacheUtils.removeFromCache(avatar_url, ImageLoader.getInstance().getMemoryCache());
                        DiskCacheUtils.removeFromCache(avatar_url, ImageLoader.getInstance().getDiskCache());
                        editor.putString("avatar", avatar_url);
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Avatar Changed Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(changeAvatar.this,account.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(changeAvatar.this);
                alertDialogBuilder.setMessage("Connection Is Unstable!");
                alertDialogBuilder.setPositiveButton("Retry",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(changeAvatar.this, features.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Home",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(changeAvatar.this, changeAvatar.class);
                                intent.putExtra("operation", "changeImg");
                                startActivity(intent);
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image=getStringImg(bitmap);
                Map<String,String> params=new HashMap<String,String>();
                params.put("img_name",image);
                params.put("user_id",userid_val);
                params.put("username",username_val);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - for image loading to heroku -> dropbox -> heroku and getting response back to app
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
                imageSet=1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void removeAvatar(final String userid_val){
        final String url="https://dytila.herokuapp.com/api/remove_avatar";
        final ProgressDialog loading=ProgressDialog.show(this,"Removing Avatar","please wait...",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        String avatar_url="https://dytila.herokuapp.com/static/img/customer_img/default.png";
                        editor.putString("avatar", avatar_url);
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Avatar Removed Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(changeAvatar.this,account.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(changeAvatar.this);
                alertDialogBuilder.setMessage("Connection Is Unstable!");
                alertDialogBuilder.setPositiveButton("Retry",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(changeAvatar.this, features.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Home",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(changeAvatar.this, changeAvatar.class);
                                intent.putExtra("operation", "removeImg");
                                startActivity(intent);
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("user_id",userid_val);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        int socketTimeout = 30000;//30 seconds - for image loading to heroku -> dropbox -> heroku and getting response back to app
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

}
