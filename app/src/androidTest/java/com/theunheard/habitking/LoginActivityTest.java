package com.theunheard.habitking;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by ian21 on 1/31/2017.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public final IntentsTestRule<LoginActivity> main = new IntentsTestRule<>(LoginActivity.class);

    private static class WaitActivityIsResumedIdlingResource implements IdlingResource {
        private final ActivityLifecycleMonitor instance;
        private final String activityToWaitClassName;
        private volatile IdlingResource.ResourceCallback resourceCallback;
        boolean resumed = false;
        public WaitActivityIsResumedIdlingResource(String activityToWaitClassName) {
            instance = ActivityLifecycleMonitorRegistry.getInstance();
            this.activityToWaitClassName = activityToWaitClassName;
        }

        @Override
        public String getName() {
            return this.getClass().getName();
        }

        @Override
        public boolean isIdleNow() {
            resumed = isActivityLaunched();
            if(resumed && resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }

            return resumed;
        }

        private boolean isActivityLaunched() {
            Collection<Activity> activitiesInStage = instance.getActivitiesInStage(Stage.RESUMED);
            for (Activity activity : activitiesInStage) {
                if(activity.getClass().getName().equals(activityToWaitClassName)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            this.resourceCallback = resourceCallback;
        }
    }

//    @Test
//    public void logicValidTest() {
//
//
//        onView(withId(R.id.email)).perform(typeText("foo@bar.com"), closeSoftKeyboard());
//        onView(withId(R.id.password)).perform(typeText("chanayya211"), closeSoftKeyboard());
//        onView(withId(R.id.email_sign_in_button)).perform(click());
//        String activityClassName = MainActivity.class.getName();
//        Espresso.registerIdlingResources(new WaitActivityIsResumedIdlingResource(activityClassName));
//        intended(hasComponent(new ComponentName(getTargetContext(), MainActivity.class)));
//
//    }



}
