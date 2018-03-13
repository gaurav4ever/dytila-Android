package com.Dytila.gauravpc.dytilasp1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class welcome_screens extends AppCompatActivity {

    CustomAdapter customAdapter;
    ViewPager viewPager;
    Button getStarted_button;
    TextView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screens);

        viewPager=(ViewPager)findViewById(R.id.view_pager);
        customAdapter=new CustomAdapter(this);
        viewPager.setAdapter(customAdapter);

    }

    public class CustomAdapter extends PagerAdapter {

        private int[] layouts={R.layout.w1,R.layout.w2,R.layout.w3,R.layout.w4,R.layout.w5};
        private LayoutInflater layoutInflater;
        private Context context;

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view==(LinearLayout)object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.invalidate();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=layoutInflater.inflate(layouts[position],container,false);
            if(position==4){
                Button button=(Button)v.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(welcome_screens.this, activity1.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            container.addView(v);
            return v;
        }
    }

}
