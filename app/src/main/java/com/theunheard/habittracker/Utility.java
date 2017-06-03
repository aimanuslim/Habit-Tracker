package com.theunheard.habittracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date.getTime());
    }

    public static String outputApproximateTimePeriodDifferenceAsString(Date startDate, Date endDate) {
        // http://stackoverflow.com/questions/21285161/android-difference-between-two-dates

        //milliseconds
        long differenceInMilliseconds = endDate.getTime() - startDate.getTime();

//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : "+ endDate);
//        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weeksInMilli = daysInMilli * 7;
        long monthsInMilli = weeksInMilli * 4;
        long yearsInMilli = monthsInMilli * 12;
        long periodCount = 0;
        String periodString = "minutes";



        if( (differenceInMilliseconds / yearsInMilli) > 0 ) {
            periodCount = differenceInMilliseconds / yearsInMilli;
            periodString = "year" + (periodCount > 1 ? "s" : "");
        } else if ((differenceInMilliseconds / monthsInMilli) > 0) {
            periodCount = differenceInMilliseconds / monthsInMilli;
            periodString = "month" + (periodCount > 1 ? "s" : "");
        } else if ( (differenceInMilliseconds / weeksInMilli) > 0 ) {
            periodCount = differenceInMilliseconds / weeksInMilli;
            periodString = "week" + (periodCount > 1 ? "s" : "");
        } else if ( (differenceInMilliseconds / daysInMilli) > 0 ) {
            periodCount = differenceInMilliseconds / daysInMilli;
            periodString = "day" + (periodCount > 1 ? "s" : "");
        } else if ( (differenceInMilliseconds / hoursInMilli) > 0 ) {
            periodCount = differenceInMilliseconds / hoursInMilli;
            periodString = "hour" + (periodCount > 1 ? "s" : "");
        } else if ( (differenceInMilliseconds / minutesInMilli) > 0 ) {
            periodCount = differenceInMilliseconds / minutesInMilli;
            periodString = "minute" + (periodCount > 1 ? "s" : "");
        } else if ( (differenceInMilliseconds / secondsInMilli) > 0 ) {
            periodCount = differenceInMilliseconds / secondsInMilli;
            periodString = "second" + (periodCount > 1 ? "s" : "");
        }

        if(periodCount == 0) {
//            Log.d("Utility", Long.toString(differenceInMilliseconds));
        }

        return Long.toString(periodCount) + " " + periodString;
    }
}
