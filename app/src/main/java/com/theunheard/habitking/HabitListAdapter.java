package com.theunheard.habitking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by ian21 on 2/4/2017.
 */

public class HabitListAdapter extends ArrayAdapter<Habit> {
    public HabitListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public HabitListAdapter(Context context, int resource, List<Habit> habits) {
        super(context, resource, habits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.habit_item, null);
        }

        Habit habit = getItem(position);

        if (habit != null) {
            TextView nameLabel = (TextView) v.findViewById(R.id.habitNamelabel);
            TextView categoryLabel = (TextView) v.findViewById(R.id.habitCategoryLabel);
            TextView periodSinceLastPerformedLabel = (TextView) v.findViewById(R.id.habitLastPerformedDate);
            TextView reminderPeriodLabel = (TextView) v.findViewById(R.id.habitReminderPeriod);

            nameLabel.setText(habit.getName());
            categoryLabel.setText(habit.getCategory());
            periodSinceLastPerformedLabel.setText("Time since last performed: " + Utility.outputApproximateTimePeriodDifferenceAsString(habit.getDateLastPerformed(), new Date()));

            reminderPeriodLabel.setText("Reminder frequency: " +habit.getReminderPeriodMultiplier().toString() + " " + habit.getReminderPerPeriodLengthModeAsString());
        }

        return v;
    }


}
