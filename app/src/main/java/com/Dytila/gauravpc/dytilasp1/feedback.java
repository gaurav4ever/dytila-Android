package com.Dytila.gauravpc.dytilasp1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.HashMap;
import java.util.Map;

public class feedback extends AppCompatActivity {

    EditText feedbackText;
    Button sendFeedback;
    RelativeLayout feedbackLayout;
    LinearLayout contactusLayout1, contactusLayout2;
    ImageView feedbackImg, contactusImg, faqsImg, goBack;
    NestedScrollView faqs, contact_view;
    int l1count=0,l2count=0,l3count=0,l4count=0,l5count=0,l6count=0;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        feedbackLayout = (RelativeLayout) findViewById(R.id.viewFeedback);
        feedbackImg = (ImageView) findViewById(R.id.feedback_img);

        contact_view = (NestedScrollView) findViewById(R.id.contactView);
        contactusImg = (ImageView) findViewById(R.id.contactus_img);

        faqs = (NestedScrollView) findViewById(R.id.viewFaqs);
        faqsImg = (ImageView) findViewById(R.id.faqs_img);

        Intent intent = getIntent();
        String type = intent.getStringExtra("value");

        Log.e("status", type);

        if (type.equals("faqs")) {

            CardView cardView=(CardView)findViewById(R.id.mainHead);
            cardView.setVisibility(View.GONE);

            feedbackLayout.setVisibility(View.GONE);
            feedbackImg.setVisibility(View.GONE);

            contactusImg.setVisibility(View.GONE);
            contact_view.setVisibility(View.GONE);

            WebView browser = (WebView) findViewById(R.id.webView);
            browser.loadUrl("https://dytila.herokuapp.com/admin/faqs");

            ImageView imageView=(ImageView)findViewById(R.id.goBackImage);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


        } else if (type.equals("contactus")) {

            feedbackLayout.setVisibility(View.GONE);
            feedbackImg.setVisibility(View.GONE);

            faqs.setVisibility(View.GONE);
            faqsImg.setVisibility(View.GONE);

            LinearLayout img1, img2, img3, img4, img5;
            img1 = (LinearLayout) findViewById(R.id.callNumber);
            img2 = (LinearLayout) findViewById(R.id.mail);
            img3 = (LinearLayout) findViewById(R.id.website);
            img4 = (LinearLayout) findViewById(R.id.facebook);
            img5 = (LinearLayout) findViewById(R.id.insta);

            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("status", "calling....");

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:9585896662"));
                    if (ActivityCompat.checkSelfPermission(feedback.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(feedback.this, Manifest.permission.CALL_PHONE)) {
                            //This is called if user has denied the permission before
                            //In this case I am just asking the permission again
                            ActivityCompat.requestPermissions(feedback.this, new String[]{Manifest.permission.CALL_PHONE}, 0x2);

                        } else {
                            ActivityCompat.requestPermissions(feedback.this, new String[]{Manifest.permission.CALL_PHONE}, 0x2);
                        }
                    }
                    else{
                        startActivity(callIntent);
                    }
                    }
                }

                );
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v){
                        Log.i("Send email", "");
                        String[] TO = {"dytilanutrition@gmail.com"};
                        String[] CC = {""};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL,TO );
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            finish();
                            Log.e("Status","Finished sending email...");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(feedback.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                }
                }

                );
                img3.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dytilanutrition.com"));
                    startActivity(intent);
                }
                }

                );
                img4.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/167551100331762"));
                    startActivity(intent);
                }
                }

                );
                img5.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick (View v){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dytila_nutrition"));
                    startActivity(intent);
                }
                }

                );

            }else{

            contactusImg.setVisibility(View.GONE);
            contact_view.setVisibility(View.GONE);

            faqs.setVisibility(View.GONE);
            faqsImg.setVisibility(View.GONE);

            sendFeedback=(Button)findViewById(R.id.send_feedback_button);

            sendFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
                    String userid_val = sharedPreferences.getString("user_id", "");
                    feedbackText = (EditText) findViewById(R.id.feedback_text);
                    String feedText = feedbackText.getText().toString();
                    boolean check = isNetworkAvailable();
                    if (check == true) {
                        giveFeedback(userid_val,feedText);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        goBack=(ImageView)findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
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
    public void giveFeedback(final String user_id,final String feedback){
        String url="https://dytila.herokuapp.com/api/feedback";
        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(feedback.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        final Dialog dialog = new Dialog(feedback.this);
                        dialog.setContentView(R.layout.layout_thankyou);
                        TextView close = (TextView) dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(feedback.this);
                alertDialogBuilder.setMessage("Connection Is Unstable!");
                alertDialogBuilder.setPositiveButton("Retry",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(feedback.this, features.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Home",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(feedback.this, features.class);
                                intent.putExtra("value", "feedback");
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
                params.put("user_id",user_id);
                params.put("feedback",feedback);
                return params;
            }
        };
        requestQueue.add(stringRequest);
        int socketTimeout = 5000;//30 seconds - for image loading to heroku -> dropbox -> heroku and getting response back to app
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}