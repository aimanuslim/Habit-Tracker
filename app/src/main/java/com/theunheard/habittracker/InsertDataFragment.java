package com.theunheard.habittracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.text.InputType.TYPE_CLASS_DATETIME;
import static android.text.InputType.TYPE_DATETIME_VARIATION_DATE;


public class InsertDataFragment extends Fragment implements FragmentInterface {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private AutoCompleteTextView habitNameTextView;
    private EditText dateTextView;
    private EditText timeTextView;
    private AutoCompleteTextView categoryTextView;
    private EditText repetitionFrequencyTextView;
    private Spinner repetitionPeriodSpinner;
    private ListView personInteractedListView;

    private Button addPersonButton;
    private Button recordButton;
    private Button nowButton;
    private Button clearPersonButton;
    private Button clearFieldButton;
    private Button testButton;
    private Runnable updateAutoCompleteComponents;

    private DBHandler _dbHandler;

    private String[] periodArray;

    // Firebase objects
    private DatabaseReference _databaseRef;
    private FirebaseDatabase _firebaseInstance;

    private final static String TAG = String.valueOf(com.theunheard.habittracker.R.string.app_name);

    // for list view
    private ArrayList<String>  person_list;
    ArrayAdapter<String> arrayAdapter;

    private ArrayList<String>  habit_name_list;
    private ArrayAdapter<String> habitAutoCompleteAdapter;
    private ArrayList<String>  category_name_list;
    private ArrayAdapter<String> categoryAutoCompleteAdapter;
    private AdView adView;

    public InsertDataFragment() {
        // Required empty public constructor
    }


    @Override
    public void fragmentBecameVisible() {
        if(_dbHandler != null) {
            getActivity().runOnUiThread(updateAutoCompleteComponents);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(com.theunheard.habittracker.R.layout.fragment_insert_data, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupAll();
    }

    private void setupAll() {

        this.periodArray = new String[] {"Minute (s)", "Hour (s)", "Day (s)", "Week (s)", "Month (s)", "Year (s)"};


        adView = (AdView) getView().findViewById(com.theunheard.habittracker.R.id.adViewInsertData);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        habitNameTextView = (AutoCompleteTextView) getView().findViewById(com.theunheard.habittracker.R.id.habitInputName);
        dateTextView = (EditText) getView().findViewById(com.theunheard.habittracker.R.id.dateLastPerformedInput);
        timeTextView = (EditText) getView().findViewById(com.theunheard.habittracker.R.id.timeLastPerformedInput);

        categoryTextView = (AutoCompleteTextView) getView().findViewById(com.theunheard.habittracker.R.id.categoryInput);
        repetitionFrequencyTextView = (EditText) getView().findViewById(com.theunheard.habittracker.R.id.repetitionFrequencyInput);
        addPersonButton = (Button) getView().findViewById(com.theunheard.habittracker.R.id.addPersonInteractedButton);
        recordButton = (Button) getView().findViewById(com.theunheard.habittracker.R.id.recordButton);
        nowButton = (Button) getView().findViewById(com.theunheard.habittracker.R.id.nowButton);
        clearPersonButton = (Button) getView().findViewById(com.theunheard.habittracker.R.id.clearPersonButton);
        clearFieldButton = (Button) getView().findViewById(com.theunheard.habittracker.R.id.clearFieldButton);
        personInteractedListView = (ListView) getView().findViewById(com.theunheard.habittracker.R.id.personInteractedListView);


        person_list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this.getActivity(), com.theunheard.habittracker.R.layout.person_name_item, person_list);
        personInteractedListView.setAdapter(arrayAdapter);

        updateAutoCompleteComponents = new Runnable() {
            public void run() {
                if(habit_name_list != null) {
                    // problem is here: http://stackoverflow.com/questions/32210404/autocompletetextview-doesnt-update-arrayadapter-items
                    // once the list is attached to adapter, the change made to the list may not reflect in the adapter because of the mOriginalValues that stick
                    // so, what I have decided to do is to find the difference between the two list (one from the original before deletion in the datalistview, the other one coming from the database), and remove the different elements one by one from the adapter.
                    ArrayList<String> dbList = _dbHandler.getNames(_dbHandler.COL_NAME);

                    if(dbList.size() < habit_name_list.size()){
                        ArrayList<String> diffList = new ArrayList<>(habit_name_list);
                        diffList.removeAll(dbList);
                        for (String removedHabitName: diffList
                                ) {
                            habitAutoCompleteAdapter.remove(removedHabitName);
                        }
                        habit_name_list = new ArrayList<>(dbList);
                    } else {
                        ArrayList<String> diffList = new ArrayList<>(dbList);
                        diffList.removeAll(habit_name_list);
                        for (String insertedHabitName: diffList
                                ) {
                            habitAutoCompleteAdapter.add(insertedHabitName);
                        }
                        habit_name_list = new ArrayList<>(dbList);
                    }

                }
                if(category_name_list != null) {
                    ArrayList<String> dbList = _dbHandler.getNames(_dbHandler.COL_CATEGORY);

                    if(dbList.size() < category_name_list.size()){
                        ArrayList<String> diffList = new ArrayList<>(category_name_list);
                        diffList.removeAll(dbList);
                        for (String removedCategoryName: diffList
                                ) {
                            categoryAutoCompleteAdapter.remove(removedCategoryName);
                        }
                        category_name_list = new ArrayList<>(dbList);
                    } else {
                        ArrayList<String> diffList = new ArrayList<>(dbList);
                        diffList.removeAll(category_name_list);
                        for (String insertedCategoryName: diffList
                                ) {
                            categoryAutoCompleteAdapter.add(insertedCategoryName);
                        }
                        category_name_list = new ArrayList<>(dbList);
                    }


//                    categoryTextView.invalidate();
//                    category_name_list.clear();
//                    category_name_list.addAll(_dbHandler.getNames(_dbHandler.COL_CATEGORY));
//                    categoryAutoCompleteAdapter.notifyDataSetChanged();
//                    categoryTextView.setAdapter(categoryAutoCompleteAdapter);
                }

            }
        };


//        testButton = (Button) getView().findViewById(R.id.testButton);
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                notifyTest();
//            }
//        });


        _dbHandler = new DBHandler(this.getActivity());
//        _dbHandler.refreshDB();
//        _dbHandler.onCreate();

//        _dbHandler.deleteTables();
//        _dbHandler.createTable();



        // firebase initialization

        // TODO: implement fire base for analytics
//        if(_databaseRef == null) {
//            _firebaseInstance = FirebaseDatabase.getInstance();
//            _firebaseInstance.setPersistenceEnabled(true);
//            _databaseRef = _firebaseInstance.getReference();
//        }


//        DBHandler db = new DBHandler(this, null, null, 1);






        // adjust date and time form sizes

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int width = displayMetrics.widthPixels;
//        dateTextView.setWidth(width/2);
//        timeTextView.setWidth(width/2);

        setupHabitNameAutoComplete();
        setupCategoryAutoComplete();
        setupRepetitionPeriodSpinner();

        setupField(dateTextView);
        setupField(timeTextView);

        setupRepetitionFrequencyField();
        setupAddInteractedPersonButton(addPersonButton);
        setupNowButton();
        setupClearPersonButton();
        setupClearFieldsButton();
        setupRecordButton(recordButton);




    }

    private void setupClearFieldsButton(){
        try {
            clearFieldButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    habitNameTextView.setText("");
                    categoryTextView.setText("");
                    dateTextView.setText("");
                    timeTextView.setText("");
                }
            });
        } catch (Exception e) {
            Toast.makeText(InsertDataFragment.this.getActivity(), "Error clearing form inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClearPersonButton() {
        try {
            clearPersonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    person_list.clear();
                    arrayAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Toast.makeText(InsertDataFragment.this.getActivity(), "Something wrong with clearing person list", Toast.LENGTH_SHORT).show();
        }
    }


    private void setupHabitNameAutoComplete() {
        habit_name_list = _dbHandler.getNames(_dbHandler.COL_NAME);
        habitAutoCompleteAdapter = new ArrayAdapter<String>
                (this.getActivity(),android.R.layout.simple_list_item_1,habit_name_list);
        habitNameTextView.setAdapter(habitAutoCompleteAdapter);
        habitNameTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)parent.getItemAtPosition(position);
                Habit habit = _dbHandler.getHabitByName(selection);
                categoryTextView.setText(habit.getCategory());
            }
        });
    }

    private void setupCategoryAutoComplete() {
        category_name_list = _dbHandler.getNames(_dbHandler.COL_CATEGORY);
        categoryAutoCompleteAdapter = new ArrayAdapter<String>
                (this.getActivity(),android.R.layout.simple_list_item_1, category_name_list);
        categoryTextView.setAdapter(categoryAutoCompleteAdapter);
    }



    private void setupRepetitionPeriodSpinner() {
        repetitionPeriodSpinner = (Spinner) getView().findViewById(com.theunheard.habittracker.R.id.repetitionPeriodSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                com.theunheard.habittracker.R.layout.hk_spinner_unclicked_textview, periodArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(com.theunheard.habittracker.R.layout.hk_spinner_item);
        // Apply the adapter to the spinner
        repetitionPeriodSpinner.setAdapter(adapter);



    }

    private void setupRepetitionFrequencyField() {

    }




    private void setupRecordButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isNetworkAvailable()) {
                if(areEntriesValid()) {
                    saveData();
                    Toast.makeText(InsertDataFragment.this.getActivity(), com.theunheard.habittracker.R.string.save_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InsertDataFragment.this.getActivity(), com.theunheard.habittracker.R.string.missing_info, Toast.LENGTH_SHORT).show();
                }
//                } else {


//                }
            }
        });
    }


    private boolean checkLogInStatus(FirebaseUser user) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            return true;
        } else {
            // No user is signed in
            Toast.makeText(InsertDataFragment.this.getActivity(), "User not logged in!", Toast.LENGTH_SHORT);
            return false;
        }
    }




    private Date prepareDatePerformed() {
        Date convertedLastDatePerformed = Utility.stringToDate(dateTextView.getText().toString(), Utility.dateFormat);
        Date convertedLastTimePerformed = Utility.stringToDate(timeTextView.getText().toString(), Utility.timeFormat);
        Calendar d = Calendar.getInstance();
        d.setTime(convertedLastDatePerformed);
        Calendar t = Calendar.getInstance();
        t.setTime(convertedLastTimePerformed);
        Calendar finalCal = Calendar.getInstance();
        finalCal.set(Calendar.DAY_OF_MONTH, d.get(Calendar.DAY_OF_MONTH));
        finalCal.set(Calendar.MONTH, d.get(Calendar.MONTH));
        finalCal.set(Calendar.YEAR, d.get(Calendar.YEAR));
        // fam this cannot be Calendar.HOUR for some reason
        finalCal.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
        finalCal.set(Calendar.MINUTE, t.get(Calendar.MINUTE));

        return finalCal.getTime();

    }


    private void setupReminderNotification(Habit habit) {
        if(habit.getReminderPerPeriodLengthMode() == null) return;
        Date reminderTime = habit.getNextReminderTime();
        if(reminderTime != null) {
//            Log.d("Next Reminder Time:", Utility.dateToString(reminderTime, Utility.dateFormat + " " + Utility.timeFormat));
            habit.setAlarmId(_dbHandler.setAlarm(habit.getNextReminderTime().getTime(), habit.getRepeatingPeriodInMillis(), habit));
        }
    }




    private void saveData() {

        String habitName = habitNameTextView.getText().toString();
        Habit habit = new Habit(prepareDatePerformed(), habitName, "0");
        habit.setCategory(categoryTextView.getText().toString());


        if(!repetitionFrequencyTextView.getText().toString().trim().equals("") && !repetitionFrequencyTextView.getText().toString().trim().equals("0")) {
            int deltaTime = Integer.parseInt(repetitionFrequencyTextView.getText().toString());
            habit.setReminderTimeAndProperties(repetitionPeriodSpinner.getSelectedItemPosition(), deltaTime);

        }

        setupReminderNotification(habit);


        // if this habit is already in database, increase the number of times it has been performed.
        // It is only considered the same habit if both the name and the category matches whats already in the database.
        if(_dbHandler.habitExist(habitName, habit.getCategory())) {
            _dbHandler.increaseHabitFreq(habitName, habit.getCategory());
            _dbHandler.updateTimeLastPerformed(habit);
        } else {
            _dbHandler.addHabit(habit);
        }

        if(personInteractedListView.getAdapter().getCount() != 0) {
            _dbHandler.addPersonInteracted(person_list, habit.getId());
        }


        // update autocomplete components
        if(_dbHandler != null) {
            getActivity().runOnUiThread(updateAutoCompleteComponents);
        }


//        Log.d(TAG, "Done adding");
//        Log.d(TAG, "DB " + _dbHandler.databasetostring());




//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if(checkLogInStatus(user)) {
//            String uid = user.getUid().toString();
//            Habit habit = new Habit(dateToSave, habitName, uid);
//            _databaseRef.child("habits").setValue(habit);
//            Log.d(TAG, "Data saved");
//            Toast.makeText(InsertDataActivity.this, "Data saved!", Toast.LENGTH_SHORT);
//        } else {
//            Log.d(TAG, "Data not saved");
//            Toast.makeText(InsertDataActivity.this, "No date saved", Toast.LENGTH_SHORT);
//        }

    }
    private boolean areEntriesValid() {
        if(habitNameTextView.getText().toString().trim().equals("")) { return false; }
        if(dateTextView.getText().toString().trim().equals("") || timeTextView.getText().toString().trim().equals("")) { return false; }
        return true;
    }

    private void setupNowButton () {
        nowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTextView.setText(Utility.dateToString(new Date(), Utility.dateFormat));
                timeTextView.setText(Utility.dateToString(new Date(), Utility.timeFormat));

            }
        });
    }


    public void deleteDatabase() {
        _dbHandler.deleteAllHabitsAndPerson();
    }

    private void setupAddInteractedPersonButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InsertDataFragment.this.getActivity());
                    alert.setTitle(com.theunheard.habittracker.R.string.addinteractedperson_title);
                    alert.setMessage(com.theunheard.habittracker.R.string.addinteractedperson_message);

//                final EditText input = new EditText(InsertDataFragment.this.getActivity());
                    final AutoCompleteTextView input = new AutoCompleteTextView(InsertDataFragment.this.getActivity());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (getActivity(),android.R.layout.simple_list_item_1,_dbHandler.getAllPersonNames());
                    input.setAdapter(adapter);
                    input.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            input.showDropDown();
                        }
                    });
                    input.setId(com.theunheard.habittracker.R.id.person_name_edit);

                    alert.setView(input);

                    alert.setPositiveButton(com.theunheard.habittracker.R.string.button_add_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String personName = input.getEditableText().toString();
                            person_list.add(personName);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });

                    alert.setNegativeButton(com.theunheard.habittracker.R.string.button_cancel_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } catch (Exception e) {
                    Toast.makeText(InsertDataFragment.this.getActivity(), "Error: cannot add interacted person", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void hideSoftKeyboard(EditText et){
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupField(final EditText et) {

        et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideSoftKeyboard(et);
                setDialog(et);
            }
        });

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSoftKeyboard(et);
                if (hasFocus) {
                    setDialog(et);
                }
            }
        });

        et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftKeyboard(et);
                return false;
            }
        });
    }



    private void setDialog(EditText et) {
        Calendar now = Calendar.getInstance();
//        Log.d(TAG, "input type:" + et.getInputType());
        if(et.getInputType() == (TYPE_CLASS_DATETIME | TYPE_DATETIME_VARIATION_DATE)) {
            datePickerDialog = new DatePickerDialog(InsertDataFragment.this.getActivity(), new CustomSetListener(et), now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else {
            timePickerDialog = new TimePickerDialog(InsertDataFragment.this.getActivity(), new CustomSetListener(et), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        }
    }


}
