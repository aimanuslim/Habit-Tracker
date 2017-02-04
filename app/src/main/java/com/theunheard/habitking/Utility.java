package com.theunheard.habitking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ian21 on 2/4/2017.
 */

public class Utility {
    public Utility() {
    }

    public static final String dateFormat = "dd-MM-yyyy";
    public static final String timeFormat = "h:mm a";

    public static Date stringToDate(String dateString, String format) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format); // here set the pattern as you date in string was containing like date/month/year
            Date date = sdf.parse(dateString);
            return date;
        }catch(ParseException ex){
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            return null;
        }
    }

    public static String dateToString(Date date, String format) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date.getTime());
    }
}
