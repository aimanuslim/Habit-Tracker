package com.theunheard.habittracker;

import android.os.Build;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.SecureRandom;
import java.util.Date;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Math.abs;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by ian21 on 3/16/2017.
 */


@RunWith(AndroidJUnit4.class)
public class TabbedActivityTest {

    private UiDevice mDevice;
    @Rule
    public final ActivityTestRule<TabbedActivity> main = new ActivityTestRule<>(TabbedActivity.class);



    @Test
    public void shouldBeAbleToLaunchMainScreen() {

    }

    @Test
    public void pullDownNotification() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.openNotification();
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
            enterHabitDetails(randomString(), randomString(), randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
            clickRecordButton();
            clearAllInputs();
        }
//        deleteData();
    }

    public void clearAllInputs() {
        onView(withId(com.theunheard.habittracker.R.id.habitInputName)).perform(clearText());
        onView(withId(com.theunheard.habittracker.R.id.categoryInput)).perform(clearText());
        onView(withId(com.theunheard.habittracker.R.id.dateLastPerformedInput)).perform(clearText());
        onView(withId(com.theunheard.habittracker.R.id.timeLastPerformedInput)).perform(clearText());
        onView(withId(com.theunheard.habittracker.R.id.repetitionFrequencyInput)).perform(clearText());

    }

    public void clearAllPersons() {
        onView(withId(com.theunheard.habittracker.R.id.clearPersonButton)).perform(click());
    }

    public String enterHabitDetailsThatHasJustBeenPerformed(
            String habitName,
            String category,
            String repetitionFreq,
            String repetitionPeriod
    ) {
        onView(withId(com.theunheard.habittracker.R.id.habitInputName)).perform(typeText(habitName));
        Espresso.closeSoftKeyboard();
        onView(withId(com.theunheard.habittracker.R.id.categoryInput)).perform(typeText(category));
        Espresso.closeSoftKeyboard();

        onView(withText("Just did it!")).perform(click());
        onView(withId(com.theunheard.habittracker.R.id.repetitionFrequencyInput)).perform(typeText(repetitionFreq));
        Espresso.closeSoftKeyboard();



        onView(withId(com.theunheard.habittracker.R.id.repetitionPeriodSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(repetitionPeriod))).perform(click());

        return habitName;

    }

    public String enterHabitDetails(
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
        onView(withId(com.theunheard.habittracker.R.id.habitInputName)).perform(typeText(habitName));
        Espresso.closeSoftKeyboard();
        onView(withId(com.theunheard.habittracker.R.id.categoryInput)).perform(typeText(category));
        Espresso.closeSoftKeyboard();

        if(year != 0) {
            onView(withId(com.theunheard.habittracker.R.id.dateLastPerformedInput)).perform(click());
            onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month, day));
            if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                onView(withText("OK")).perform(click());
            } else {
                onView(withText("Done")).perform(click());
            }
        }

        if(hour != 0) {
            onView(withId(com.theunheard.habittracker.R.id.timeLastPerformedInput)).perform(click());
            onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hour, minutes));
            if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                onView(withText("OK")).perform(click());
            } else {
                onView(withText("Done")).perform(click());
            }
        }



        onView(withId(com.theunheard.habittracker.R.id.repetitionFrequencyInput)).perform(typeText(repetitionFreq));
        Espresso.closeSoftKeyboard();



        onView(withId(com.theunheard.habittracker.R.id.repetitionPeriodSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(repetitionPeriod))).perform(click());

        return habitName;


//        onView(withText(R.string.save_success)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed())
//        deleteData();

    }

    public void waitInSeconds (int seconds) {
        SystemClock.sleep(seconds * 1000);
    }

    public void clickRecordButton() {
//        waitInSeconds(2);
        onView(withId(com.theunheard.habittracker.R.id.recordButton)).perform(click());
    }

    @Test
    public void addHabitWithoutName () {
        enterHabitDetails("", randomString(), randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        clickRecordButton();
        onView(withText(com.theunheard.habittracker.R.string.missing_info)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        deleteData();
    }

    public void switchToDataListView() {
//        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(com.theunheard.habittracker.R.string.data_list_tab_text)).perform(click());
    }


    public void switchToInsertHabitView() {
//        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(com.theunheard.habittracker.R.string.insert_data_tab_text)).perform(click());
    }


    @Test
    public void addHabitsWithPersonInteracted() {
        switchToInsertHabitView();
        String habitname = randomString();
        enterHabitDetails(habitname, randomString(), randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        String personName1 = enterPersonInteracted();
        String personName2 = enterPersonInteracted();
        clickRecordButton();

        switchToDataListView();
        switchDataListToPersonList();
        checkIfPersonItemExist(habitname, personName1, personName2);
        switchToInsertHabitView();
        clearAllInputs();
        clearAllPersons();
    }

    @Test
    public void addMultipleHabitWithPerson()  {
        for( int i = 0; i < 5; i++ )
            addHabitsWithPersonInteracted();
    }

    public void checkIfPersonItemExist (String habitName, String... persons) {
        for (String personName: persons) {
//            onView(allOf(withText(personName), withText(habitName), withParent(withId(R.id.dataListView))));
//            onView(allOf(withId(R.id.dataListView), isDescendantOfA(firstChildOf(withId(R.id.container))), withText(personName))).check(matches(isDisplayed()));
            onData(withYourPersonName(Matchers.containsString(personName)));
//
////                            inAdapterView(withId(R.id.dataListView))
//                    .check(matches(isDisplayed()));
//            Log.d("Check person", "Succeed " + personName);

        }

    }

    // custom matcher for pager
    public static Matcher<View> firstChildOf(final Matcher<View> parentMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with first child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {

                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }
                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(0).equals(view);

            }
        };
    }

    // custom matcher to find the habit name text in listview
    public static Matcher<Object> withYourHabitName(final Matcher<String> habitName) {
        checkNotNull(habitName);
        return new BoundedMatcher<Object, Habit>(Habit.class) {
            @Override
            public boolean matchesSafely(Habit habit) {
                return habitName.matches(habit.getName());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with text: " + habitName.toString());
                habitName.describeTo(description);
            }
        };
    }

    // custom matcher to find the person in the text in listview
    public static Matcher<Object> withYourPersonName(final Matcher<String> personName) {
        checkNotNull(personName);
        return new BoundedMatcher<Object, Person>(Person.class) {
            @Override
            public boolean matchesSafely(Person person) {
                return personName.matches(person.getName());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with text: " + personName.toString());
                personName.describeTo(description);
            }
        };
    }

    // from https://google.github.io/android-testing-support-library/docs/espresso/advanced/index.html#asserting-that-a-data-item-is-not-in-an-adapter
    private static Matcher<View> withAdaptedData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public void switchDataListToPersonList () {
        onView(withId(com.theunheard.habittracker.R.id.dataModeSpinner)).perform(click());
        onView(withText("Person List")).perform(click());


    }

    public void switchDataListToHabitList () {
        onView(withId(com.theunheard.habittracker.R.id.dataModeSpinner)).perform(click());
        onView(withText("Habit List")).perform(click());
    }

    public String enterPersonInteracted() {
        onView(withId(com.theunheard.habittracker.R.id.addPersonInteractedButton)).perform(click());
        String personName = randomString();
        onView(withId(com.theunheard.habittracker.R.id.person_name_edit)).perform(typeText(personName), closeSoftKeyboard());
        waitInSeconds(1);
        onView(withText("Add")).perform(click());
        return personName;
    }






    @Test
    public void addHabitAndTestNotificationAlarm () {
        String habitName = enterHabitDetailsThatHasJustBeenPerformed("TestNotification", "", "1", spinnerOptions[0]);
        clickRecordButton();
        waitInSeconds(75);
        pullDownNotification();

        UiObject2 title = mDevice.findObject(By.text("Hey"));
        assertEquals("Hey", title.getText() );
        title.click();

        switchToDataListView();
        deleteHabitFromList(habitName);

        waitInSeconds(75);
        pullDownNotification();



    }

    @Test
    public void addHabitWithoutCategory () {
        enterHabitDetails(randomString(), "", randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        clickRecordButton();
        onView(withText(com.theunheard.habittracker.R.string.save_success)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        deleteData();
    }

    @Test
    public void addHabitWithoutDate () {
        enterHabitDetails(randomString(), randomString(), 0, randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        clickRecordButton();
        onView(withText(com.theunheard.habittracker.R.string.missing_info)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        deleteData();
    }

    @Test
    public void addHabitWithoutTime () {
        enterHabitDetails(randomString(), randomString(), randomInt(2017), randomInt(12), randomInt(29), 0, randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        clickRecordButton();
        onView(withText(com.theunheard.habittracker.R.string.missing_info)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        deleteData();
    }

    @Test
    public void addHabitWithoutRepetitionFreq () {
        enterHabitDetails(randomString(), randomString(), randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), "", spinnerOptions[randomInt(spinnerOptions.length)]);
        clickRecordButton();
        onView(withText(com.theunheard.habittracker.R.string.save_success)).inRoot(withDecorView(not(is(main.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        deleteData();
    }
    @Test
    public void nowButtonCheck() {
        onView(withId(com.theunheard.habittracker.R.id.nowButton)).perform(click());
        onView(withId(com.theunheard.habittracker.R.id.dateLastPerformedInput)).check(matches(withText(Utility.dateToString(new Date(), Utility.dateFormat))));
        onView(withId(com.theunheard.habittracker.R.id.timeLastPerformedInput)).check(matches(withText(Utility.dateToString(new Date(), Utility.timeFormat))));

    }



    @Test
    public void increaseFrequencyCheck()
    {
        enterHabitDetails("Test", "Test_category", randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        clearAllInputs();
        enterHabitDetails("Test", "Test_category", randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("View My Habits")).perform(click());
        onData(anything()).inAdapterView(withId(com.theunheard.habittracker.R.id.dataListView)).atPosition(0).onChildView(withId(com.theunheard.habittracker.R.id.frequencyPerformedLabel)).check(matches(withText("2 times")));
        deleteData();


    }



    public void deleteData() {
        DBHandler dbHandler = main.getActivity().getDB();
        dbHandler.deleteAllHabitsAndPerson();
        dbHandler.deleteAllPersons();
    }


    @Test
    public void deleteHabitAndMakeSurePersonInteractedGotDeleted() {
        String habitName = enterHabitDetails(randomString(), randomString(), randomInt(2017), randomInt(12), randomInt(29), randomInt(24), randomInt(60), Integer.toString(randomInt(10)), spinnerOptions[randomInt(spinnerOptions.length)]);
        String personName = enterPersonInteracted();
        clickRecordButton();
        switchToDataListView();
        deleteHabitFromList(habitName);
        switchDataListToPersonList();
        // negative testing.
        onView(withId(com.theunheard.habittracker.R.id.dataListView)).check(matches(not(withAdaptedData(withYourPersonName(Matchers.containsString(personName))))));

    }






    public void deleteHabitFromList(String habitName) {
        onData(withYourHabitName(Matchers.containsString(habitName)))
                .inAdapterView(withId(com.theunheard.habittracker.R.id.dataListView))
                .perform(click());
        onView(withText("Delete")).perform(click());
    }
}
