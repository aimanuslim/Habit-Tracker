package com.theunheard.habitking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ian21 on 1/25/2017.
 */

public class CustomSetListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public final EditText editTextToModify;
    public CustomSetListener(EditText et) {
        this.editTextToModify = et;
    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        DateFormat dateFormatter = new SimpleDateFormat(Utility.dateFormat, Locale.US);
        Calendar dateSelected = Calendar.getInstance();
        dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
        editTextToModify.setText(dateFormatter.format(dateSelected.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        DateFormat timeFormatter = new SimpleDateFormat(Utility.timeFormat, Locale.US);
        Calendar timeSelected = Calendar.getInstance();
        timeSelected.set(timeSelected.get(Calendar.YEAR), timeSelected.get(Calendar.MONTH), timeSelected.get(Calendar.DATE), hourOfDay, minute);
        editTextToModify.setText(timeFormatter.format(timeSelected.getTime()));
    }
}




