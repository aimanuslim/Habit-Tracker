package com.theunheard.habitking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.text.InputType.TYPE_CLASS_DATETIME;
import static android.text.InputType.TYPE_DATETIME_VARIATION_DATE;

public class MainActivity extends AppCompatActivity {


    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private EditText habitNameTextView;
    private EditText dateTextView;
    private EditText timeTextView;
    private EditText categoryTextView;
    private EditText repetitionFrequencyTextView;
    private Spinner repetitionPeriodSpinner;

    private Button addPersonButton;
    private Button recordButton;

    private DBHandler _dbHandler;


    // Firebase objects
    private DatabaseReference _databaseRef;
    private FirebaseDatabase _firebaseInstance;

    private final static String TAG = "HabitKing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        habitNameTextView = (EditText) findViewById(R.id.habitInputName);
        dateTextView = (EditText) findViewById(R.id.dateLastPerformedInput);
        timeTextView = (EditText) findViewById(R.id.timeLastPerformedInput);

        categoryTextView = (EditText) findViewById(R.id.categoryInput);
        repetitionFrequencyTextView = (EditText) findViewById(R.id.repetitionFrequencyInput);
        addPersonButton = (Button) findViewById(R.id.addPersonInteractedButton);
        recordButton = (Button) findViewById(R.id.recordButton);

        _dbHandler = new DBHandler(this);


        // firebase initialization

        // TODO: skip firebase and figure out how to use local storage for now
//        if(_databaseRef == null) {
//            _firebaseInstance = FirebaseDatabase.getInstance();
//            _firebaseInstance.setPersistenceEnabled(true);
//            _databaseRef = _firebaseInstance.getReference();
//        }


//        DBHandler db = new DBHandler(this, null, null, 1);
//        db.deleteTables();





        // adjust date and time form sizes

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        dateTextView.setWidth(width/2);
        timeTextView.setWidth(width/2);


        setupRepetitionPeriodSpinner();

        setupField(dateTextView);
        setupField(timeTextView);

        setupAddInteractedPersonButton(addPersonButton);
        setupRecordButton(recordButton);



    }

    private void setupRepetitionPeriodSpinner() {
        repetitionPeriodSpinner = (Spinner) findViewById(R.id.repetitionPeriodSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.repetition_period_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        repetitionPeriodSpinner.setAdapter(adapter);



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setupRecordButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isNetworkAvailable()) {
                if(areEntriesValid()) {
                    saveToCloud();
                } else {
                    Log.d(TAG, "Entries not valid");
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
            Toast.makeText(MainActivity.this, "User not logged in!", Toast.LENGTH_SHORT);
            return false;
        }
    }


    private Date stringToDate(String dateString, String format) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format); // here set the pattern as you date in string was containing like date/month/year
            Date date = sdf.parse(dateString);
            return date;
        }catch(ParseException ex){
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            return null;
        }
    }

    private Date prepareDatePerformed() {
        Date convertedLastDatePerformed = stringToDate(dateTextView.getText().toString(), CustomSetListener.dateFormat);
        Date convertedLastTimePerformed = stringToDate(timeTextView.getText().toString(), CustomSetListener.timeFormat);
        Calendar d = Calendar.getInstance();
        d.setTime(convertedLastDatePerformed);
        Calendar t = Calendar.getInstance();
        t.setTime(convertedLastTimePerformed);
        Calendar finalCal = Calendar.getInstance();
        finalCal.set(Calendar.DAY_OF_MONTH, d.get(Calendar.DAY_OF_MONTH));
        finalCal.set(Calendar.MONTH, d.get(Calendar.MONTH));
        finalCal.set(Calendar.YEAR, d.get(Calendar.YEAR));
        finalCal.set(Calendar.HOUR, t.get(Calendar.HOUR));
        finalCal.set(Calendar.MINUTE, t.get(Calendar.MINUTE));

        return finalCal.getTime();

    }


    private long calculatePeriodInMinutes() {
        int itemPos = repetitionPeriodSpinner.getSelectedItemPosition();
        long multiplier;
        switch(itemPos) {
            case 0: // minute
                multiplier = 1;
                return multiplier;
            case 1: // hour
                multiplier = 1 * 60;
                return multiplier;
            case 2: // day
                multiplier = 1 * 60 * 24;
                return multiplier;
            case 3: // week
                multiplier = 1 * 60 * 24 * 7;
                return multiplier;
            case 4: // month
                multiplier = 1 * 60 * 24 * 7 * 30;
                return multiplier;
            case 5: // year
                multiplier = 1 * 60 * 24 * 7 * 30 * 12;
                return multiplier;
            default: return 0;
        }
    }
    
    private void saveToCloud() {
        // TODO: save data to firebase
        String habitName = habitNameTextView.getText().toString();
        Habit habit = new Habit(prepareDatePerformed(), habitName, "0");
        if(categoryTextView.getText().toString() != "") {
            habit.setCategory(categoryTextView.getText().toString());
        }

        if(repetitionFrequencyTextView.getText().toString() != "") {
            habit.setRepetitionPeriod(calculatePeriodInMinutes());
        }




        _dbHandler.addHabits(habit);
        Log.d(TAG, "Done adding");
        Log.d(TAG, "DB " + _dbHandler.databasetostring());


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if(checkLogInStatus(user)) {
//            String uid = user.getUid().toString();
//            Habit habit = new Habit(dateToSave, habitName, uid);
//            _databaseRef.child("habits").setValue(habit);
//            Log.d(TAG, "Data saved");
//            Toast.makeText(MainActivity.this, "Data saved!", Toast.LENGTH_SHORT);
//        } else {
//            Log.d(TAG, "Data not saved");
//            Toast.makeText(MainActivity.this, "No date saved", Toast.LENGTH_SHORT);
//        }

    }
    private boolean areEntriesValid() {
        if(habitNameTextView.getText().toString().trim().length() == 0) { return false; }
        if(dateTextView.getText().toString().trim().length() == 0 && timeTextView.getText().toString().trim().length() == 0) { return false; }
        return true;
    }


    private void setupAddInteractedPersonButton(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Add person");
                alert.setMessage("Enter the name of the person you performed this activity with");

                final EditText input = new EditText(MainActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String personName = input.getEditableText().toString();
                        TextView tv = (TextView) new TextView(MainActivity.this);
                        LinearLayout ll = (LinearLayout) findViewById(R.id.interactedPersonList);
                        tv.setText(personName);
                        ll.addView(tv);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });
    }

    private void hideSoftKeyboard(EditText et){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
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
            datePickerDialog = new DatePickerDialog(MainActivity.this, new CustomSetListener(et), now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else {
            timePickerDialog = new TimePickerDialog(MainActivity.this, new CustomSetListener(et), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        }
    }

}

