package com.mapps.doodlchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.support.v4.content.res.ResourcesCompat.getDrawable;

public class HomeActivity extends FragmentActivity {

    ViewPager viewPager;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.activity_home);
        viewPager.setAdapter(myAdapter);
        viewPager.setCurrentItem(1);
    }
}
