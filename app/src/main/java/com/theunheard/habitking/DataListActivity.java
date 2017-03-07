package com.theunheard.habitking;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class DataListActivity extends AppCompatActivity {


    private ListView dataListView;
    private Spinner dataModeSpinner;
    private DBHandler _dbHandler;
    private HabitListAdapter habitListAdapter;
    private ArrayList<Habit> habitList;

    private ArrayList<Person> personList;
    private PersonListAdapter personListAdapter;




    private final static String[] dataModeArray = new String[] {"Habit List", "Person List"};

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

        dataListView = (ListView) findViewById(R.id.dataListView);
        dataModeSpinner = (Spinner) findViewById(R.id.dataModeSpinner);
        _dbHandler = new DBHandler(this);


        setupAdapters();
       setupHabitAdapter();

        setupDataSelector();



    }

    private void setupAdapters() {
        habitList = _dbHandler.getAllHabits();
        habitListAdapter = new HabitListAdapter(this, R.layout.habit_item, habitList);
        personList = _dbHandler.getAllPerson();
        personListAdapter = new PersonListAdapter(this, R.layout.person_item, personList);



    }

    private void setupDataSelector() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataModeArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dataModeSpinner.setAdapter(adapter);

        dataModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("position", "Position: " + Integer.toString(position));
                switch (position) {
                    case 0:
                        setupHabitAdapter();
                        break;
                    case 1:
                        setupPersonInteractedAdapter();
                        break;
                    default: setupHabitAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setupPersonInteractedAdapter() {
//        dataListView.setAdapter(null);

        dataListView.setAdapter(personListAdapter);
        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                final Dialog dialog = new Dialog(DataListActivity.this);
                dialog.setContentView(R.layout.fragment_edit_person_info_fragment_dialog);

                // TODO: this should probably be autocompletetextview
                final EditText personName = (EditText) dialog.findViewById(R.id.personEdit_personNameEditText);
                // TODO: this should be a spinner
                final EditText associatedHabit = (EditText) dialog.findViewById(R.id.editPerson_associatedHabitEditText);

                Button updateButton = (Button) dialog.findViewById(R.id.personItemUpdateButton);
                Button deleteButton = (Button) dialog.findViewById(R.id.personItemDeletedButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.personItemCancelButton);

                final Person person = (Person) dataListView.getItemAtPosition(pos);

                personName.setText(person.getName());
                associatedHabit.setText(person.getHabitName());



                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        person.setName(personName.getText().toString());
                        person.setHabitName(associatedHabit.getText().toString());
                        personListAdapter.notifyDataSetChanged();
                        _dbHandler.modifyPerson(person);
                        dialog.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                deleteButton.setTag(pos);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        personList.remove(position);
                        personListAdapter.notifyDataSetChanged();
                        _dbHandler.deletePerson(Integer.parseInt(person.getId()));
                        dialog.dismiss();
                    }
                });


//                myDialog.show(fm, "Edit Habit");

                dialog.show();

            }
        });


    }


    private void setupHabitAdapter() {
//        dataListView.setAdapter(null);
        dataListView.setAdapter(habitListAdapter);
        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                android.app.FragmentManager fm = getFragmentManager();
//                EditHabitFragmentDialog myDialog = new EditHabitFragmentDialog().newInstance("Edit habit");

                final Dialog dialog = new Dialog(DataListActivity.this);
                dialog.setContentView(R.layout.fragment_edit_habit_fragment_dialog);
                dialog.setTitle("Edit Habit");
//                View dialogView = view.inflate(getApplicationContext(), R.layout.fragment_edit_habit_fragment_dialog, null);

                final EditText name = (EditText) dialog.findViewById(R.id.habitName_DialogInput);
                final EditText category = (EditText) dialog.findViewById(R.id.category_DialogInput);
                final EditText mult = (EditText) dialog.findViewById(R.id.multiplier_DialogInput);
                final Spinner periodSpinner = (Spinner) dialog.findViewById(R.id.reminderPeriod_DialogSpinner);
                Button updateButton = (Button) dialog.findViewById(R.id.updateButton);
                Button deleteButton = (Button) dialog.findViewById(R.id.deleteButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);



                final Habit habit = (Habit) dataListView.getItemAtPosition(pos);

                name.setText(habit.getName());
                category.setText(habit.getCategory());
                mult.setText(habit.getReminderPeriodMultiplier().toString());
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DataListActivity.this,
                        R.array.repetition_period_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                periodSpinner.setAdapter(adapter);
                periodSpinner.setSelection(habit.getReminderPerPeriodLengthMode());



                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        habit.setName(name.getText().toString());
                        habit.setCategory(category.getText().toString());
                        habit.setReminderPeriodProperties(periodSpinner.getSelectedItemPosition(), Integer.parseInt(mult.getText().toString()));
                        habitListAdapter.notifyDataSetChanged();
                        _dbHandler.modifyHabit(habit);
                        dialog.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                deleteButton.setTag(pos);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        habitList.remove(position);
                        habitListAdapter.notifyDataSetChanged();
                        _dbHandler.deleteHabit(Integer.parseInt(habit.getId()));
                        dialog.dismiss();
                    }
                });


//                myDialog.show(fm, "Edit Habit");

                dialog.show();

            }
        });

        habitListAdapter.notifyDataSetChanged();
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
