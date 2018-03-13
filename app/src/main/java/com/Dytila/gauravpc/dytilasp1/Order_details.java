package com.Dytila.gauravpc.dytilasp1;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.Dytila.gauravpc.dytilasp1.fragmentHandler.FragmentPageAdapter_terms_and_privacy;
import com.Dytila.gauravpc.dytilasp1.fragmentHandler.Fragment_monthly_orders;
import com.Dytila.gauravpc.dytilasp1.fragmentHandler.Fragment_regular_orders;

public class Order_details extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        addFragments(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(listener(viewPager));


    }
    private void addFragments(ViewPager viewPager) {
        FragmentPageAdapter_terms_and_privacy adapter = new FragmentPageAdapter_terms_and_privacy(getSupportFragmentManager());
        adapter.addFragment(new Fragment_regular_orders(), "Regular Orders");
        adapter.addFragment(new Fragment_monthly_orders(), "Monthly Orders ");
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
