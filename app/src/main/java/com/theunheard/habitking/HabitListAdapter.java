package com.theunheard.habitking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            TextView lastPerformedLabel = (TextView) v.findViewById(R.id.habitLastPerformedDate);
            TextView reminderPeriodLabel = (TextView) v.findViewById(R.id.habitReminderPeriod);

            nameLabel.setText(habit.getName());
            categoryLabel.setText(habit.getCategory());
            lastPerformedLabel.setText(habit.getDateTimeLastPerformedAsString());
            reminderPeriodLabel.setText(habit.getReminderPeriodMultiplier().toString() + " " + habit.getReminderPerPeriodLength());
        }

        return v;
    }
}