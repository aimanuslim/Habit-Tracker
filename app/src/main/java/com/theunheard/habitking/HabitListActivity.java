package com.theunheard.habitking;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HabitListActivity extends AppCompatActivity {


    private ListView habitListView;
    private DBHandler _dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.habitron_habit_list_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        habitListView = (ListView) findViewById(R.id.habitListView);
        _dbHandler = new DBHandler(this);

       setupHabitListView();



    }

    private void setupHabitListView() {
        final ArrayList<Habit> habitList = _dbHandler.getAllHabits();
        HabitListAdapter adapter = new HabitListAdapter(this, R.layout.habit_item, habitList);
        habitListView.setAdapter(adapter);
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String habit = (String) habitListView.getItemAtPosition(i);

                android.app.FragmentManager fm = getFragmentManager();
                EditHabitFragmentDialog myDialog = new EditHabitFragmentDialog().newInstance("Edit habit");
                myDialog.show(fm, "Edit Habit");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.habitlist_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home) {

                // User chose the "Settings" item, show the app settings UI...
                finish();



        }
        return true;
    }
}
