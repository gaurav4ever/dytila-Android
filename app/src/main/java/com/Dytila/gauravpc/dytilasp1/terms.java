package com.Dytila.gauravpc.dytilasp1;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.Dytila.gauravpc.dytilasp1.fragmentHandler.FragmentPageAdapter_terms_and_privacy;
import com.Dytila.gauravpc.dytilasp1.fragmentHandler.Fragment_open_source_licenses;
import com.Dytila.gauravpc.dytilasp1.fragmentHandler.Fragment_privacy;
import com.Dytila.gauravpc.dytilasp1.fragmentHandler.Fragment_terms;

public class terms extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        addFragments(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(listener(viewPager));

    }
    private void addFragments(ViewPager viewPager) {
        FragmentPageAdapter_terms_and_privacy adapter = new FragmentPageAdapter_terms_and_privacy(getSupportFragmentManager());
        adapter.addFragment(new Fragment_terms(), "Terms and Conditions");
        adapter.addFragment(new Fragment_privacy(), "Privacy Policy ");
        adapter.addFragment(new Fragment_open_source_licenses(),"Open Source Licenses");
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
