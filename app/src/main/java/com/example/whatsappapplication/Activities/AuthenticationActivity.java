package com.example.whatsappapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.whatsappapplication.R;
import com.example.whatsappapplication.TabsAdapter.AuthenticationTabAdapter;
import com.example.whatsappapplication.TabsAdapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        ViewPager viewPager = findViewById(R.id.view_pager1);
        viewPager.setAdapter(new AuthenticationTabAdapter(getSupportFragmentManager(), FragmentPagerAdapter.POSITION_NONE));
        TabLayout tabs = findViewById(R.id.tabs1);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar1));
        tabs.setupWithViewPager(viewPager);

    }
}