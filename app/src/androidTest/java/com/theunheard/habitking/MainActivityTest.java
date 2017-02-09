package com.theunheard.habitking;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.intent.IntentCallback;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.SecureRandom;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Math.abs;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by ian21 on 1/31/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest  {
    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainScreen() {

    }

    static final String alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static String[] spinnerOptions = {"Minute (s)", "Hour (s)", "Day (s)", "Week (s)", "Month (s)", "Year (s)"};
    static SecureRandom rnd = new SecureRandom();

    String randomString(  ){
        StringBuilder sb = new StringBuilder( 10 );
        for( int i = 0; i < 10; i++ )
            sb.append( alphanumeric.charAt( rnd.nextInt(alphanumeric.length()) ) );
        return sb.toString();
    }

    int randomInt( int range ) {
        return abs(rnd.nextInt() % (range));
    }

    @Test
    public void addMultipleHabits () {

        for(int i = 0; i < 5; i++) {
            addHabits(randomString(), randomString(), randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
            clearAllInputs();
        }
    }

    public void clearAllInputs() {
        onView(withId(R.id.habitInputName)).perform(clearText());
        onView(withId(R.id.categoryInput)).perform(clearText());
        onView(withId(R.id.dateLastPerformedInput)).perform(clearText());
        onView(withId(R.id.timeLastPerformedInput)).perform(clearText());
        onView(withId(R.id.repetitionFrequencyInput)).perform(clearText());

    }


    public void addHabits(
            String habitName,
            String category,
            int year,
            int month,
            int day,
            int hour,
            int minutes,
            String repetitionFreq,
            String repetitionPeriod
    ) {
        onView(withId(R.id.habitInputName)).perform(typeText(habitName));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.categoryInput)).perform(typeText(category));
        Espresso.closeSoftKeyboard();

        if(year != 0) {
            onView(withId(R.id.dateLastPerformedInput)).perform(click());
            onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
            onView(withText("OK")).perform(click());
        }

        if(hour != 0) {
            onView(withId(R.id.timeLastPerformedInput)).perform(click());
            onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hour, minutes));
            onView(withText("OK")).perform(click());
        }



        onView(withId(R.id.repetitionFrequencyInput)).perform(typeText(repetitionFreq));
        Espresso.closeSoftKeyboard();



        onView(withId(R.id.repetitionPeriodSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(repetitionPeriod))).perform(click());
        SystemClock.sleep(2000);
        onView(withId(R.id.recordButton)).perform(click());

//        onView(withText(R.string.save_success)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed())
        deleteData();

    }

    @Test
    public void addHabitWithoutName () {
        addHabits("", randomString(), randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        onView(withText(R.string.missing_info)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }


    @Test
    public void addHabitWithoutCategory () {
        addHabits(randomString(), "", randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        onView(withText(R.string.save_success)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void addHabitWithoutDate () {
        addHabits(randomString(), randomString(), 0, randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        onView(withText(R.string.missing_info)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void addHabitWithoutTime () {
        addHabits(randomString(), randomString(), randomInt(2017), randomInt(12), randomInt(29), 0, randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        onView(withText(R.string.missing_info)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void addHabitWithoutRepetitionFreq () {
        addHabits(randomString(), randomString(), randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), "", spinnerOptions[randomInt(spinnerOptions.length)]);
        onView(withText(R.string.save_success)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }


    public void deleteData() {
        DBHandler dbHandler = main.getActivity().getDB();
        dbHandler.deleteAllHabits();
    }

}
