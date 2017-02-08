package com.theunheard.habitking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ian21 on 1/23/2017.
 */

public class Habit {
    private String ownerUid;
    private String id;
    private Date dateLastPerformed;
    private String name;
    private String category;
    private Integer frequencyPerformed; // TODO: frequency of action tracked independently since its recorded into the database, rememember to implement this functionality
    private Date nextReminderTime; // date when the user will be reminded of the habit, will be calculated using the reminder properties
    private Integer reminderPerPeriodLengthMode; // ex: weeks, months

    public void setReminderPeriodMultiplier(Integer reminderPeriodMultiplier) {
        this.reminderPeriodMultiplier = reminderPeriodMultiplier;
    }

    private Integer reminderPeriodMultiplier;
    private ArrayList<String> personsInteracted;

    public final static int PERIOD_MINUTE = 0;
    public final static int PERIOD_HOUR = 1;
    public final static int PERIOD_DAY = 2;
    public final static int PERIOD_WEEK = 3;
    public final static int PERIOD_MONTH = 4;
    public final static int PERIOD_YEAR = 5;


    public Habit() {
    }

    public Habit(Date dateLastPerformed, String name, String ownerUid) {
        this.dateLastPerformed = dateLastPerformed;
        this.name = name;
        this.ownerUid = ownerUid;
        this.category = null;
        this.frequencyPerformed = 0;
        this.personsInteracted = null;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReminderTimeAndProperties(int periodSpinnerPosition, int deltaTime) {
        // TODO: test next reminder time functionality
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateLastPerformed.getTime());
        switch(periodSpinnerPosition) {
            case PERIOD_MINUTE: // minute
                cal.add(Calendar.MINUTE, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties(periodSpinnerPosition, deltaTime);
                break;
            case PERIOD_HOUR: // hour
                cal.add(Calendar.HOUR, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties(periodSpinnerPosition , deltaTime);
                break;
            case PERIOD_DAY: // day
                cal.add(Calendar.DATE, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties(periodSpinnerPosition, deltaTime);
                break;
            case PERIOD_WEEK: // week
                cal.add(Calendar.WEEK_OF_YEAR, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties(periodSpinnerPosition, deltaTime);
                break;
            case PERIOD_MONTH: // month
                cal.add(Calendar.MONTH, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties(periodSpinnerPosition, deltaTime);
                break;
            case PERIOD_YEAR: // year
                cal.add(Calendar.YEAR, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties(periodSpinnerPosition, deltaTime);
                break;
            default: return;
        }
    }

    public String getPeriodString(int mode) {
        switch(mode){
            case PERIOD_MINUTE: return "minute" + (reminderPeriodMultiplier > 1 ? "s" : "");
            case PERIOD_HOUR: return "hour" + (reminderPeriodMultiplier > 1 ? "s" : "");
            case PERIOD_DAY: return "day" + (reminderPeriodMultiplier > 1 ? "s" : "");
            case PERIOD_WEEK: return "week" + (reminderPeriodMultiplier > 1 ? "s" : "");
            case PERIOD_MONTH: return "month" + (reminderPeriodMultiplier > 1 ? "s" : "");
            case PERIOD_YEAR: return "year" + (reminderPeriodMultiplier > 1 ? "s" : "");
        }
        return "";
    }

    public Integer getReminderPeriodMultiplier() {
        return reminderPeriodMultiplier;
    }

    public Integer getReminderPerPeriodLengthMode() {
        return reminderPerPeriodLengthMode;
    }
    public String getReminderPerPeriodLengthModeAsString() {
        return getPeriodString(getReminderPerPeriodLengthMode());
    }

    public void setReminderPeriodProperties(int pos, Integer mult) {
        this.reminderPeriodMultiplier = mult;
        this.reminderPerPeriodLengthMode = pos;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public String getCategory() {

        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getFrequencyPerformed() {
        return frequencyPerformed;
    }

    public void setFrequencyPerformed(Integer frequencyPerformed) {
        this.frequencyPerformed = frequencyPerformed;
    }

    public ArrayList<String> getPersonsInteracted() {
        return personsInteracted;
    }

    public void setPersonsInteracted(ArrayList<String> personsInteracted) {
        this.personsInteracted = personsInteracted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateLastPerformed() {
        return dateLastPerformed;
    }

    public void setDateLastPerformed(Date dateLastPerformed) {
        this.dateLastPerformed = dateLastPerformed;
    }

    public String getDateTimeLastPerformedAsString() {
        Date date = getDateLastPerformed();
        String dateString =  Utility.dateToString(date, Utility.dateFormat);
        dateString += " " + Utility.dateToString(date, Utility.timeFormat);
        return dateString;
    }
}
