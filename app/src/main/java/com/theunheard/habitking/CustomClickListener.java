package com.theunheard.habitking;

import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Created by ian21 on 1/25/2017.
 */

public class CustomClickListener implements DialogInterface.OnClickListener {

    public final EditText _editText;
    public CustomClickListener(EditText et) {
        _editText = et;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        return;
    }
}
