package com.theunheard.habitking;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.ThrowOnExtraProperties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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

    @Test
    public void addingHabits() {
        onView(withId())
    }
}
