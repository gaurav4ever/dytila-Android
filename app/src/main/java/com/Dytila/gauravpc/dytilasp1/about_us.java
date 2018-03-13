package com.Dytila.gauravpc.dytilasp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class about_us extends AppCompatActivity {

    ImageView back,img1,img2,img3,img4,img5,img6;
    TextView homeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        img1=(ImageView)findViewById(R.id.legal);
        img2=(ImageView)findViewById(R.id.partners);
        img3=(ImageView)findViewById(R.id.team);
        img4=(ImageView)findViewById(R.id.faqs);
        img5=(ImageView)findViewById(R.id.feedback);
        img6=(ImageView)findViewById(R.id.contactus);

        back=(ImageView)findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(about_us.this,terms.class);
                startActivity(intent);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(about_us.this,partners.class);
                startActivity(intent);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(about_us.this,team.class);
                startActivity(intent);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(about_us.this,feedback.class);
                intent.putExtra("value","faqs");
                startActivity(intent);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(about_us.this,feedback.class);
                intent.putExtra("value","feedback");
                startActivity(intent);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(about_us.this,feedback.class);
                intent.putExtra("value","contactus");
                startActivity(intent);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
