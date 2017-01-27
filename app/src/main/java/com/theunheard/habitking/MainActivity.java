package com.theunheard.habitking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

import static android.text.InputType.TYPE_CLASS_DATETIME;
import static android.text.InputType.TYPE_DATETIME_VARIATION_DATE;

public class MainActivity extends AppCompatActivity {


    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private EditText dateTextView;
    private EditText timeTextView;
    private EditText reminderDateTextView;
    private EditText reminderTimeTextView;
    private Button addPersonButton;

    private final static String TAG = "HabitKing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        dateTextView = (EditText) findViewById(R.id.dateLastPerformedInput);
        timeTextView = (EditText) findViewById(R.id.timeLastPerformedInput);
        reminderDateTextView = (EditText) findViewById(R.id.reminderDateInput);
        reminderTimeTextView = (EditText) findViewById(R.id.reminderTimeInput);
        addPersonButton = (Button) findViewById(R.id.addPersonInteractedButton);





        // adjust date and time form sizes

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        dateTextView.setWidth(width/2);
        timeTextView.setWidth(width/2);
        reminderDateTextView.setWidth(width/2);
        reminderTimeTextView.setWidth(width/2);


        setupField(dateTextView);
        setupField(timeTextView);
        setupField(reminderDateTextView);
        setupField(reminderTimeTextView);

        setupAddInteractedPersonButton(addPersonButton);



    }

    private void setupAddInteractedPersonButton(Button btn) {

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

