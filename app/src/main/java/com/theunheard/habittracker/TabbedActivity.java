package com.theunheard.habittracker;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;

public class TabbedActivity extends AppCompatActivity {

    private DBHandler _dbHandler;

    public DBHandler getDB () {
        return _dbHandler;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.theunheard.habittracker.R.layout.activity_tabbed);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Coreo");
//        setSupportActionBar(toolbar);


        TabLayout tabLayout = (TabLayout) findViewById(com.theunheard.habittracker.R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(com.theunheard.habittracker.R.string.about_app_tab_text));
        tabLayout.addTab(tabLayout.newTab().setText(com.theunheard.habittracker.R.string.insert_data_tab_text));
        tabLayout.addTab(tabLayout.newTab().setText(com.theunheard.habittracker.R.string.data_list_tab_text));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(com.theunheard.habittracker.R.id.container);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        _dbHandler = new DBHandler(this);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                FragmentInterface fragment = (FragmentInterface) adapter.instantiateItem(viewPager, tab.getPosition());
                if(fragment != null) {
                    fragment.fragmentBecameVisible();

                }

                View focus = getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                FragmentInterface fragment = (FragmentInterface) adapter.instantiateItem(viewPager, tab.getPosition());
                if(fragment != null) {
                    fragment.fragmentBecameVisible();
                }

                View focus = getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                FragmentInterface fragment = (FragmentInterface) adapter.instantiateItem(viewPager, tab.getPosition());
                if(fragment != null) {
                    fragment.fragmentBecameVisible();

                }
                View focus = getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
            }
        });
    }


    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.theunheard.habittracker.R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



}
