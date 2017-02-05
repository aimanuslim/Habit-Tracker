package com.theunheard.habitking;

import android.app.Dialog;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

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
        final HabitListAdapter habitListAdapter = new HabitListAdapter(this, R.layout.habit_item, habitList);
        habitListView.setAdapter(habitListAdapter);
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                android.app.FragmentManager fm = getFragmentManager();
//                EditHabitFragmentDialog myDialog = new EditHabitFragmentDialog().newInstance("Edit habit");

                final Dialog dialog = new Dialog(HabitListActivity.this);
                dialog.setContentView(R.layout.fragment_edit_habit_fragment_dialog);
                dialog.setTitle("Edit Habit");
//                View dialogView = view.inflate(getApplicationContext(), R.layout.fragment_edit_habit_fragment_dialog, null);

                EditText name = (EditText) dialog.findViewById(R.id.habitName_DialogInput);
                EditText category = (EditText) dialog.findViewById(R.id.category_DialogInput);
                EditText mult = (EditText) dialog.findViewById(R.id.multiplier_DialogInput);
                Spinner periodSpinner = (Spinner) dialog.findViewById(R.id.reminderPeriod_DialogSpinner);
                Button updateButton = (Button) dialog.findViewById(R.id.updateButton);
                Button deleteButton = (Button) dialog.findViewById(R.id.deleteButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);



                Habit habit = (Habit) habitListView.getItemAtPosition(pos);

                name.setText(habit.getName());
                category.setText(habit.getCategory());
                mult.setText(habit.getReminderPeriodMultiplier().toString());
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(HabitListActivity.this,
                        R.array.repetition_period_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                periodSpinner.setAdapter(adapter);
                periodSpinner.setSelection(habit.getReminderPerPeriodLengthMode());


                // TODO: implement onclick for the rest of the buttons
                deleteButton.setTag(pos);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        habitList.remove(position);
                        habitListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });


//                myDialog.show(fm, "Edit Habit");

                dialog.show();

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
