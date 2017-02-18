package com.theunheard.habitking;

import java.util.Date;

/**
 * Created by ian21 on 2/18/2017.
 */

public class Person {

    private String name;
    private String habitName;
    private Date lastDateInteractedWith;

    public Person() {
    }

    public Person(String habitName, Date lastDateInteractedWith, String name) {
        this.habitName = habitName;
        this.lastDateInteractedWith = lastDateInteractedWith;
        this.name = name;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public Date getLastDateInteractedWith() {
        return lastDateInteractedWith;
    }

    public void setLastDateInteractedWith(Date lastDateInteractedWith) {
        this.lastDateInteractedWith = lastDateInteractedWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
