package com.theunheard.saigono;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ian21 on 2/4/2017.
 */

public class HabitListAdapter extends ArrayAdapter<Habit> implements Filterable{

    private ArrayList<Habit> habitList;
    private ArrayList<Habit> filteredHabitList;
    private Filter habitFilter;

    public HabitListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public HabitListAdapter(Context context, int resource, ArrayList<Habit> habits) {
        super(context, resource, habits);
        this.habitList = habits;
        this.filteredHabitList = habits;

    }


    public void add(Habit habits) {
        habitList.add(habits);
        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public int getCount() {
        return filteredHabitList.size();
    }

    @Override
    public Habit getItem(int position) {
        return filteredHabitList.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

//    public Filter getFilter() {
//        return new Filter () {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();
//
//                if(constraint == null || constraint.length() == 0) {
//                    ArrayList<Habit> list = new ArrayList<Habit>(habitList);
//                    results.values = list;
//                    results.count = list.size();
//                } else {
//                    ArrayList<Habit> newValues = new ArrayList<Habit>();
//                    for(int i = 0; i < habitList.size(); i++) {
//                        Habit habit = habitList.get(i);
//                        if(habit.getName().equals(constraint) ||
//                            habit.getCategory().equals(constraint)
//
//                                ) {
//                            newValues.add(habit);
//                        }
//                    }
//                    results.values = newValues;
//                    results.count = newValues.size();
//                }
//                return results;
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint,
//                    FilterResults results) {
//                filteredHabitList = (List<Habit>) results.values;
////                Log.d("CustomArrayAdapter", String.valueOf(results.values));
////                Log.d("CustomArrayAdapter", String.valueOf(results.count));
//                notifyDataSetChanged();
//            }
//
//        };
//    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//
//        if (v == null) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(getContext());
//            v = vi.inflate(R.layout.habit_item, null);
//        }
//
//        Habit habit = getItem(position);
//
//        if (habit != null) {
//            TextView nameLabel = (TextView) v.findViewById(R.id.habitNamelabel);
//            TextView categoryLabel = (TextView) v.findViewById(R.id.habitCategoryLabel);
//            TextView periodSinceLastPerformedLabel = (TextView) v.findViewById(R.id.habitLastPerformedDate);
//            TextView reminderPeriodLabel = (TextView) v.findViewById(R.id.habitReminderPeriod);
//            TextView frequencyPerformedLabel = (TextView) v.findViewById(R.id.frequencyPerformedLabel);
//
//
//            nameLabel.setText(habit.getName());
//            categoryLabel.setText(habit.getCategory());
//            periodSinceLastPerformedLabel.setText("Last performed: " + Utility.outputApproximateTimePeriodDifferenceAsString(habit.getDateLastPerformed(), new Date()));
//            frequencyPerformedLabel.setText(habit.getFrequencyPerformed().toString() + " time" + (habit.getFrequencyPerformed() > 1 ? "s" : ""));
//
//            reminderPeriodLabel.setText(habit.getReminderPeriodMultiplier().toString() + " " + habit.getReminderPerPeriodLengthModeAsString());
//        }
//
//        return v;
//    }

    static class ViewHolder {
        TextView nameLabel;
        TextView categoryLabel;
        TextView periodSinceLastPerformedLabel;
        TextView reminderPeriodLabel;
        TextView frequencyPerformedLabel;

    }

    @Override
    public Filter getFilter() {
        if (habitFilter == null) {
            habitFilter = new HabitFilter();
        }

        return habitFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        final Habit habit = (Habit) getItem(position);


        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.habit_item, null);
            holder = new ViewHolder();
            holder.nameLabel = (TextView) v.findViewById(R.id.habitNamelabel);
            holder.categoryLabel = (TextView) v.findViewById(R.id.habitCategoryLabel);
            holder.periodSinceLastPerformedLabel = (TextView) v.findViewById(R.id.habitLastPerformedDate);
            holder.reminderPeriodLabel = (TextView) v.findViewById(R.id.habitReminderPeriod);
            holder.frequencyPerformedLabel = (TextView) v.findViewById(R.id.frequencyPerformedLabel);
            v .setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }



        if (habit != null) {
            holder.nameLabel.setText(habit.getName());
            holder.categoryLabel.setText(habit.getCategory());
            holder.periodSinceLastPerformedLabel.setText("Last performed: " + Utility.outputApproximateTimePeriodDifferenceAsString(habit.getDateLastPerformed(), new Date()));
            holder.frequencyPerformedLabel.setText("Performed " + habit.getFrequencyPerformed().toString() + " time" + (habit.getFrequencyPerformed() > 1 ? "s" : ""));
            holder.reminderPeriodLabel.setText("Remind every " + habit.getReminderPeriodMultiplier().toString() + " " + habit.getReminderPerPeriodLengthModeAsString());
        }

        return v;
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class HabitFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<Habit> tempList = new ArrayList<Habit>();

                // search content in friend list
                for (Habit habit: habitList) {
                    if (habit.getName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            habit.getCategory().toLowerCase().contains(constraint.toString().toLowerCase())
                            ) {
                        tempList.add(habit);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = habitList.size();
                filterResults.values = habitList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            habitList = (ArrayList<Habit>) results.values;
            notifyDataSetChanged();
//            clear();
            for(int i = 0, l = habitList.size(); i < l; i++) {
                add(habitList.get(i));
            }
//            notifyDataSetInvalidated();

        }
    }



//    private class HabitFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results = new FilterResults();
//
//            if (constraint == null || constraint.length() == 0) {
//                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(
//                        mAllData);
//                results.values = list;
//                results.count = list.size();
//            } else {
//                ArrayList<HashMap<String, String>> newValues = new ArrayList<HashMap<String, String>>();
//                for (int i = 0; i < mAllData.size(); i++) {
//                    HashMap<String, String> item = mAllData.get(i);
//                    if (item.get(JsonFragmentCategoriaCONN.TAG_NOME)
//                            .toLowerCase()
//                            .contains(constraint.toString().toLowerCase())) {
//                        newValues.add(item);
//                    }
//                }
//                results.values = newValues;
//                results.count = newValues.size();
//            }
//
//            return results;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint,
//                                      FilterResults results) {
//            mDataShown = (ArrayList<HashMap<String, String>>) results.values;
//            Log.d("CustomArrayAdapter", String.valueOf(results.values));
//            Log.d("CustomArrayAdapter", String.valueOf(results.count));
//            notifyDataSetChanged();
//        }
//
//    }

}
