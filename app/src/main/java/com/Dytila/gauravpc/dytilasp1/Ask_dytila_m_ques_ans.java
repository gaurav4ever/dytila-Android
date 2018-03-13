package com.Dytila.gauravpc.dytilasp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.Dytila.gauravpc.dytilasp1.fragmentHandler.FragmentPageAdapter_terms_and_privacy;
import com.Dytila.gauravpc.dytilasp1.fragmentHandler.Fragment_ask_dytila_m_ans;
import com.Dytila.gauravpc.dytilasp1.fragmentHandler.Fragment_ask_dytila_m_ques;

public class Ask_dytila_m_ques_ans extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_dytila_m_ques_ans);

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        addFragments(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(listener(viewPager));

        //askDYtila options
        RelativeLayout feedl,askl,ansl,blogl,maccl;
        feedl=(RelativeLayout)findViewById(R.id.img1);
        askl=(RelativeLayout)findViewById(R.id.img2);
        ansl=(RelativeLayout)findViewById(R.id.img3);
        maccl=(RelativeLayout)findViewById(R.id.img4);

        feedl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Ask_dytila_m_ques_ans.this,askDytilaMain.class);
                startActivity(intent);
                finish();
            }
        });
        askl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ask_dytila_m_ques_ans.this, askDytila_ask.class);
                startActivity(intent);
                finish();
            }
        });
        ansl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ask_dytila_m_ques_ans.this, askDytila_que.class);
                startActivity(intent);
                finish();
            }
        });
        maccl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ask_dytila_m_ques_ans.this, Ask_dytila_m_ques_ans.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void addFragments(ViewPager viewPager) {
        FragmentPageAdapter_terms_and_privacy adapter = new FragmentPageAdapter_terms_and_privacy(getSupportFragmentManager());
        adapter.addFragment(new Fragment_ask_dytila_m_ques(), "My Questions");
        adapter.addFragment(new Fragment_ask_dytila_m_ans(), "My Answers ");
        viewPager.setAdapter(adapter);
    }
    private TabLayout.OnTabSelectedListener listener(final ViewPager pager){
        return new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
        };
    }

}
