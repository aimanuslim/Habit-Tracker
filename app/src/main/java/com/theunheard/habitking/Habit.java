package com.theunheard.habitking;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ian21 on 1/23/2017.
 */

public class Habit {
    private String ownerUid;
    private Date dateLastPerformed;
    private String name;
    private String category;
    private Integer frequencyPerformed;
    private long repetitionPeriod;
    private ArrayList<String> personsInteracted;

    public Habit() {
    }

    public Habit(Date dateLastPerformed, String name, String ownerUid) {
        this.dateLastPerformed = dateLastPerformed;
        this.name = name;
        this.ownerUid = ownerUid;
        this.category = null;
        this.frequencyPerformed = 0;
        this.repetitionPeriod = 0;
        this.personsInteracted = null;

    }

    public long getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(long repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
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
}
