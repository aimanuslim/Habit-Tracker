package com.theunheard.saigono;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;



public class DataListFragment extends Fragment implements FragmentInterface {


    private ListView dataListView;
    private Spinner dataModeSpinner;
    private DBHandler _dbHandler;
    private HabitListAdapter habitListAdapter;
    private ArrayList<Habit> habitList;

    private ArrayList<Person> personList;
    private PersonListAdapter personListAdapter;
    private Runnable updateListAndAdapters;
    private Runnable notifyDataSetChangedFromMainThread;
    private Button clearDataButton;
    private SearchView searchView;
    private Button sortButton;
    private ListView sortTypeListView;


    private ArrayAdapter<String> habitSortAdapter;
    private ArrayAdapter<String> personSortAdapter;
    private ArrayList<String> sortModeList;
    private AdView adView;





//    private final static String[] dataModeArray = new String[] {"Habit List", "Person List"};
    private static String[] dataModeArray;
//    private static String[] habitSortModeArray = new String[] {"Last Performed", "Name"};
    private static String[] habitSortModeArray;
//    private static String[] personSortModeArray = new String[] {"Person Name", "Last Performed With"};
    private static String[] personSortModeArray;

    public DataListFragment() {
        // Required empty public constructor
    }

    @Override
    public void fragmentBecameVisible() {
        if(_dbHandler != null) {
            getActivity().runOnUiThread(updateListAndAdapters);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupAll();
    }

    public void setupAll () {
        adView = (AdView) getView().findViewById(R.id.adViewDataList);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        dataListView = (ListView) getView().findViewById(R.id.dataListView);
        dataModeSpinner = (Spinner) getView().findViewById(R.id.dataModeSpinner);
        _dbHandler = new DBHandler(this.getActivity());
        searchView = (SearchView) getView().findViewById(R.id.dataListSearchView);
        dataModeArray = getResources().getStringArray(R.array.data_mode_array);
        personSortModeArray = getResources().getStringArray(R.array.person_sort_mode_array);
        habitSortModeArray = getResources().getStringArray(R.array.habit_sort_mode_array);
        sortButton = (Button) getView().findViewById(R.id.sortButton);

        updateListAndAdapters = new Runnable() {
            public void run() {
                //reload content
//                dataListView.invalidateViews();
//                dataListView.refreshDrawableState();

//                http://stackoverflow.com/questions/14503006/android-listview-not-refreshing-after-notifydatasetchanged
                // problem with reference to adapter for the listview.
                personList.clear();
                personList.addAll(_dbHandler.getAllPerson());
                personListAdapter.notifyDataSetChanged();

                habitList.clear();
                habitList.addAll(_dbHandler.getAllHabits());
                habitListAdapter.notifyDataSetChanged();


            }
        };

        notifyDataSetChangedFromMainThread = new  Runnable() {
            public void run() {
                personListAdapter.notifyDataSetChanged();
                habitListAdapter.notifyDataSetChanged();
            }
        };



        setupAdapters();
        setupHabitAdapter();

        setupDataModeSpinner();

        setupClearDataButton();
        setupSearchView();
        setupSortButton();


    }

    public void setupSortModeListView (final Dialog dialog) {
        sortTypeListView = (ListView) dialog.findViewById(R.id.sortModeListView);

        if(dataListView.getAdapter() == habitListAdapter) {
            sortTypeListView.setAdapter(habitSortAdapter);
        } else {
            sortTypeListView.setAdapter(personSortAdapter);
        }



        sortTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                try {
                    String sortModeTextSelected = (String) sortTypeListView.getItemAtPosition(position);
                    if(sortTypeListView.getAdapter() == habitSortAdapter) {
                        if(sortModeTextSelected.equals(getResources().getString(R.string.name_habit_sort_ascending_mode_text))) {
                            Collections.sort(habitList, new HabitNameComparator());
                            getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
                        } else if(sortModeTextSelected.equals(getResources().getString(R.string.name_habit_sort_descending_mode_text))) {
                            Collections.sort(habitList, Collections.<Habit>reverseOrder(new HabitNameComparator()));
                            getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
                        } else if(sortModeTextSelected.equals(getResources().getString(R.string.last_performed_habit_sort_ascending_mode_text))) {
                            Collections.sort(habitList, new  HabitLastPerformedComparator());
                            getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
                        } else if (sortModeTextSelected.equals(getResources().getString(R.string.last_performed_habit_sort_descending_mode_text))) {
                            Collections.sort(habitList, Collections.<Habit>reverseOrder(new  HabitLastPerformedComparator()));
                            getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
                        }
                    } else {
                        if(sortModeTextSelected.equals(getResources().getString(R.string.last_performed_person_sort_ascending_mode_text))){
                            Collections.sort(personList, new  PersonLastPerformedComparator());
                            getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
                        } else if (sortModeTextSelected.equals(getResources().getString(R.string.last_performed_person_sort_descending_mode_text))){
                            Collections.sort(personList, Collections.<Person>reverseOrder(new  PersonLastPerformedComparator()));
                            getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
                        } else if (sortModeTextSelected.equals(getResources().getString(R.string.name_person_sort_ascending_mode_text))) {
                            Collections.sort(personList, new PersonNameComparator());
                            getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
                        } else if (sortModeTextSelected.equals(getResources().getString(R.string.name_person_sort_descending_mode_text))) {
                            Collections.sort(personList, Collections.<Person>reverseOrder(new PersonNameComparator()));
                            getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
                        }
                    }
                    dialog.dismiss();
                } catch (Resources.NotFoundException e) {
                    Toast.makeText(DataListFragment.this.getActivity(), "Something wrong with sort feature", android.widget.Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void setupSearchView () {

        try {
            searchView.setMaxWidth( Integer.MAX_VALUE );
            searchView.setMinimumWidth(getView().findViewById(R.id.dataListSpinnerContainer).getWidth());
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String searchText) {
                    filterData(searchText);
                    return true;
                }

            });

            searchView.setMaxWidth(getView().findViewById(R.id.dataListSpinnerContainer).getWidth());

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    getActivity().runOnUiThread(updateListAndAdapters);
                    searchView.onActionViewCollapsed();
                    dataModeSpinner.setVisibility(View.VISIBLE);
                    sortButton.setVisibility(View.VISIBLE);
                    return true;
                }
            });


            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataModeSpinner.setVisibility(View.GONE);
                    sortButton.setVisibility(View.GONE);
//                    Log.d("Search View", "onSearchClickListener");
                    searchView.setMaxWidth(getView().findViewById(R.id.dataListSpinnerContainer).getWidth());
    //                searchView.setMinimumWidth(getView().findViewById(R.id.dataListSpinnerContainer).getWidth());
    //                searchView.onActionViewExpanded();

                }
            });
        } catch (Exception e) {
            Toast.makeText(DataListFragment.this.getActivity(), "Something wrong with search view", android.widget.Toast.LENGTH_LONG).show();
        }
    }

    // http://stackoverflow.com/questions/26919099/implement-search-in-listview-inside-fragment
    public void filterData(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        if(dataListView.getAdapter() == habitListAdapter) {
            habitList.clear();
            if (charText.length() == 0) {
                habitList.addAll(_dbHandler.getAllHabits());
            } else {
                for (Habit habit: _dbHandler.getAllHabits()) {
                    if (habit.getName().toLowerCase(Locale.getDefault())
                            .contains(charText) ||
                            habit.getCategory().toLowerCase(Locale.getDefault()).contains(charText)
                            ) {
                        habitList.add(habit);
                    }
                }
            }
        } else if(dataListView.getAdapter() == personListAdapter) {
            personList.clear();
            if (charText.length() == 0) {
                personList.addAll(_dbHandler.getAllPerson());
            } else {
                for (Person person: _dbHandler.getAllPerson()) {
                    if (person.getName().toLowerCase(Locale.getDefault())
                            .contains(charText) ||
                            person.getHabitName().toLowerCase(Locale.getDefault()).contains(charText)
                            ) {
                        personList.add(person);
                    }
                }
            }
        }
        getActivity().runOnUiThread(notifyDataSetChangedFromMainThread);
    }



    private void setupClearDataButton() {
        clearDataButton = (Button) getActivity().findViewById(R.id.clearDataListButton);
        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    switch(dataModeSpinner.getSelectedItemPosition()) {
                        case 0: _dbHandler.deleteAllHabitsAndPerson();
                                break;
                        case 1: _dbHandler.deleteAllPersons();
                                break;
                    }
                    getActivity().runOnUiThread(updateListAndAdapters);
                } catch (Exception e) {
                    Toast.makeText(DataListFragment.this.getActivity(), "Something wrong clearing data", android.widget.Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void setupAdapters() {
        try {
            habitList = _dbHandler.getAllHabits();
            habitListAdapter = new HabitListAdapter(this.getActivity(), R.layout.habit_item, habitList);
            personList = _dbHandler.getAllPerson();
            personListAdapter = new PersonListAdapter(this.getActivity(), R.layout.person_item, personList);
        } catch (Exception e) {
            Toast.makeText(DataListFragment.this.getActivity(), "Something wrong with fetching data", android.widget.Toast.LENGTH_LONG).show();
        }


    }

    private void setupSortButton() {

        sortModeList = new ArrayList<String>();
        // Create an ArrayAdapter using the string array and a default spinner layout
        habitSortAdapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.sort_mode_item, habitSortModeArray);
        personSortAdapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.sort_mode_item, personSortModeArray);

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    final Dialog dialog = new Dialog(DataListFragment.this.getActivity());
                    dialog.setContentView(R.layout.fragment_sort_mode_list);
                    dialog.setTitle("Select Sorting Mode");

                    setupSortModeListView(dialog);

                    dialog.show();
                } catch (Exception e) {
                    Toast.makeText(DataListFragment.this.getActivity(), "Something wrong with sort feature", android.widget.Toast.LENGTH_LONG).show();
                }


            }
        });





    }

    public static class PersonNameComparator implements  Comparator<Person> {
        @Override
        public int compare(Person person1, Person person2) {
            if(person1.getName().compareTo(person2.getName()) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static class PersonLastPerformedComparator implements  Comparator <Person> {

        @Override
        public int compare(Person person1, Person person2) {
            if(person1.getLastDateInteractedWith().compareTo(person2.getLastDateInteractedWith()) > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }


    public static class HabitNameComparator implements Comparator<Habit> {
        @Override
        public int compare(Habit habit1, Habit habit2) {
            if(habit1.getName().compareTo(habit2.getName()) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }


    public static class HabitLastPerformedComparator implements  Comparator<Habit> {
        @Override
        public int compare(Habit habit1, Habit habit2) {
            if(habit1.getDateLastPerformed().compareTo(habit2.getDateLastPerformed()) > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }



    private void setupDataModeSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.hk_spinner_unclicked_textview, dataModeArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.hk_spinner_item);
        // Apply the adapter to the spinner
        dataModeSpinner.setAdapter(adapter);

        dataModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

//                Log.d("position", "Position: " + Integer.toString(position));
                try {
                    switch (position) {
                        // TODO: fix ordering of sorting modes.
                        case 0:
                            setupHabitAdapter();
    //                        sortTypeListView.setAdapter(habitSortAdapter);
                            break;
                        case 1:
                            setupPersonInteractedAdapter();
    //                        sortTypeListView.setAdapter(personSortAdapter);
                            break;
                        default: setupHabitAdapter();
                    }
                } catch (Exception e) {
                    Toast.makeText(DataListFragment.this.getActivity(), "Something wrong with mode selection", android.widget.Toast.LENGTH_LONG).show();
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
                final Dialog dialog = new Dialog(DataListFragment.this.getActivity());
                dialog.setContentView(R.layout.fragment_edit_person_info_fragment_dialog);
                dialog.setTitle("Edit Person");

                // TODO: this should probably be autocompletetextview
                final AutoCompleteTextView personName = (AutoCompleteTextView) dialog.findViewById(R.id.personEdit_personNameEditText);
                // TODO: this should be a spinner
                final Spinner associatedHabitSpinner = (Spinner) dialog.findViewById(R.id.editPerson_associatedHabitSpinner);
                final Person person = (Person) dataListView.getItemAtPosition(pos);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getActivity(),android.R.layout.simple_list_item_1,_dbHandler.getAllHabitNames());
                associatedHabitSpinner.setAdapter(adapter);
                associatedHabitSpinner.setSelection(adapter.getPosition(person.getHabitName()));

                final Button updateButton = (Button) dialog.findViewById(R.id.personItemUpdateButton);
                Button deleteButton = (Button) dialog.findViewById(R.id.personItemDeletedButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.personItemCancelButton);

                personName.setText(person.getName());
//                associatedHabitSpinner.setText(person.getHabitName());

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        person.setName(personName.getText().toString());
                        person.setHabitName(associatedHabitSpinner.getSelectedItem().toString());
                        _dbHandler.modifyPerson(person);
                        getActivity().runOnUiThread(updateListAndAdapters);
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
                        _dbHandler.deletePersonByID(Integer.parseInt(person.getId()));
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

                try {
                    final TextView lastPerformedTextView = (TextView) view.findViewById(R.id.habitLastPerformedDate);
                    final TextView reminderFrequencyTextView = (TextView) view.findViewById(R.id.habitReminderPeriod);
                    final Dialog dialog = new Dialog(DataListFragment.this.getActivity());
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
                    Button justNowButton = (Button) dialog.findViewById(R.id.justDidItFragmentButton);


                    Habit tempHabit = (Habit) dataListView.getItemAtPosition(pos);
                    tempHabit = _dbHandler.getHabitByName(tempHabit.getName());
                    final Habit habit = tempHabit;



                    name.setText(habit.getName());
                    category.setText(habit.getCategory());
                    if(habit.getReminderPeriodMultiplier() != null) {
                        mult.setText(habit.getReminderPeriodMultiplier().toString());
                    }
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DataListFragment.this.getActivity(),
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
                            if(!mult.toString().trim().equals("")){
                                habit.setReminderPeriodProperties(periodSpinner.getSelectedItemPosition(), Integer.parseInt(mult.getText().toString()));
                                if(habit.getAlarmId() != habit.NO_INT_VALUE) {
                                    _dbHandler.updateAlarm(habit.getAlarmId(), habit.getNextReminderTime().getTime(), habit.getRepeatingPeriodInMillis(), habit);
                                }  else {
                                    habit.setAlarmId(_dbHandler.setAlarm(habit.getNextReminderTime().getTime(), habit.getRepeatingPeriodInMillis(), habit));
                                }
                            }

                            _dbHandler.modifyHabit(habit);
    //                        reminderFrequencyTextView.setText(habit.getReminderPeriodMultiplier().toString() + " " + habit.getReminderPerPeriodLengthModeAsString());
                            getActivity().runOnUiThread(updateListAndAdapters);
                            dialog.dismiss();
                            Toast.makeText(DataListFragment.this.getContext(), "Activity information updated!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    justNowButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            habit.setDateLastPerformed(new Date());
                            habit.increaseFrequencyPerformed();
                            if(!mult.toString().trim().equals("")){
                                if(habit.getAlarmId() != habit.NO_INT_VALUE) {
                                    habit.setReminderPeriodProperties(periodSpinner.getSelectedItemPosition(), Integer.parseInt(mult.getText().toString()));
                                    _dbHandler.updateAlarm(habit.getAlarmId(), habit.getNextReminderTime().getTime(), habit.getRepeatingPeriodInMillis(), habit);
                                }  else {
                                    habit.setAlarmId(_dbHandler.setAlarm(habit.getNextReminderTime().getTime(), habit.getRepeatingPeriodInMillis(), habit));
                                }
                            }
                            _dbHandler.modifyHabit(habit);
                            getActivity().runOnUiThread(updateListAndAdapters);
                            Toast.makeText(DataListFragment.this.getContext(), "Activity information updated!", Toast.LENGTH_SHORT).show();
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
                            if(_dbHandler.requestIdExists(habit.getAlarmId())){
                                _dbHandler.cancelAlarm(habit.getAlarmId());
                            }
                            _dbHandler.deleteHabitByID(Integer.parseInt(habit.getId()));
                            _dbHandler.deletePersonByHabitID(Integer.parseInt(habit.getId()));
                            getActivity().runOnUiThread(updateListAndAdapters);
                            dialog.dismiss();
                            Toast.makeText(DataListFragment.this.getContext(), "Activity deleted!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // TODOS: alarm request, how to get already existing intents?
                    // TODOS: alarm request, test when application is closed.


//                myDialog.show(fm, "Edit Habit");

                    dialog.show();
                } catch (Exception e) {
                    Toast.makeText(DataListFragment.this.getActivity(), "Something wrong with data list", android.widget.Toast.LENGTH_LONG).show();
                }
            }
        });

        habitListAdapter.notifyDataSetChanged();
    }




}
