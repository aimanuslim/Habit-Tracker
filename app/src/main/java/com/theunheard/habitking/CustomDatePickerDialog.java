package com.theunheard.habitking;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

/**
 * Created by ian21 on 1/25/2017.
 */

public class CustomDatePickerDialog extends DatePickerDialog {
    public final EditText editTextToModify;

    public CustomDatePickerDialog(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth, EditText et) {
        super(context, listener, year, month, dayOfMonth);
        editTextToModify = et;
    }
}
