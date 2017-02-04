package com.theunheard.habitking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ian21 on 1/23/2017.
 */

public class Habit {
    private String ownerUid;
    private Date dateLastPerformed;
    private String name;
    private String category;
    private Integer frequencyPerformed; // TODO: frequency of action tracked independently since its recorded into the database, rememember to implement this functionality
    private Date nextReminderTime; // date when the user will be reminded of the habit, will be calculated using the reminder properties
    private String reminderPerPeriodLength; // ex: weeks, months
    private Integer reminderPeriodMultiplier;
    private ArrayList<String> personsInteracted;

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

    public void setReminderTimeAndProperties(int periodSpinnerPosition, int deltaTime) {
        // TODO: test next reminder time functionality
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateLastPerformed.getTime());
        switch(periodSpinnerPosition) {
            case 0: // minute
                cal.add(Calendar.MINUTE, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties("minute" + (deltaTime > 1 ? "s" : ""), deltaTime);
                break;
            case 1: // hour
                cal.add(Calendar.HOUR, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties("hour" + (deltaTime > 1 ? "s" : ""), deltaTime);
                break;
            case 2: // day
                cal.add(Calendar.DATE, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties("day" + (deltaTime > 1 ? "s" : ""), deltaTime);
                break;
            case 3: // week
                cal.add(Calendar.WEEK_OF_YEAR, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties("week" + (deltaTime > 1 ? "s" : ""), deltaTime);
                break;
            case 4: // month
                cal.add(Calendar.MONTH, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties("month" + (deltaTime > 1 ? "s" : ""), deltaTime);
                break;
            case 5: // year
                cal.add(Calendar.YEAR, deltaTime);
                nextReminderTime = cal.getTime();
                setReminderPeriodProperties("year" + (deltaTime > 1 ? "s" : ""), deltaTime);
                break;
            default: return;
        }
    }

    public Integer getReminderPeriodMultiplier() {
        return reminderPeriodMultiplier;
    }

    public String getReminderPerPeriodLength() {
        return reminderPerPeriodLength;
    }

    public void setReminderPeriodProperties(String length, Integer mult) {
        this.reminderPeriodMultiplier = mult;
        this.reminderPerPeriodLength = length;
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
