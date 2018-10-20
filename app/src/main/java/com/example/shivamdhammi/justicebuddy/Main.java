package com.example.shivamdhammi.justicebuddy;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shivamdhammi.justicebuddy.Fragmentstab.MyCases;
import com.example.shivamdhammi.justicebuddy.Fragmentstab.Socialize;
import com.example.shivamdhammi.justicebuddy.Fragmentstab.crowd_funding;
import com.example.shivamdhammi.justicebuddy.Fragmentstab.document_advisor;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

public class Main extends AppCompatActivity {


    private TabAdapter adapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());






        adapter.addFragment(new Socialize(), "Socialize");
        adapter.addFragment(new document_advisor(), "D-Advisor");
        adapter.addFragment(new crowd_funding(), "Funding");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));


    }
}
