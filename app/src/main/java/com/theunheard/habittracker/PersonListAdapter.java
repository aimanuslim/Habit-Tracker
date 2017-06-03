package com.theunheard.habittracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by ian21 on 2/18/2017.
 */

public class PersonListAdapter extends ArrayAdapter<Person> {
    public PersonListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public PersonListAdapter(Context context, int resource, List<Person> personList) {
        super(context, resource, personList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(com.theunheard.habittracker.R.layout.person_item, null);
        }

        Person person = getItem(position);

        if (person != null) {
            TextView nameLabel = (TextView) v.findViewById(com.theunheard.habittracker.R.id.personNameTextView);
            TextView habitLabel = (TextView) v.findViewById(com.theunheard.habittracker.R.id.associatedHabitTextView);
            TextView dateLastInteractedWithLabel = (TextView) v.findViewById(com.theunheard.habittracker.R.id.lastInteractedWithTextView);

            nameLabel.setText(person.getName());
            habitLabel.setText(person.getHabitName());
//            dateLastInteractedWithLabel.setText("Last Interacted with: " + Utility.outputApproximateTimePeriodDifferenceAsString(person.getLastDateInteractedWith(), new Date()));
            dateLastInteractedWithLabel.setText(this.getContext().getString(com.theunheard.habittracker.R.string.listviewitem_last_interacted_with_label, Utility.outputApproximateTimePeriodDifferenceAsString(person.getLastDateInteractedWith(), new Date())));

        }

        return v;
    }
}
