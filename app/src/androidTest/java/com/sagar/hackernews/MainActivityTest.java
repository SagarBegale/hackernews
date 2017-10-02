package com.sagar.hackernews;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sagar.hacknews.test.utils.RecyclerViewItemCountAssertion;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Sagar
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        Thread.sleep(2000);

    }

    @Test
    public void normalExecution_NoErrorMessageShown() {
        onView(withId(R.id.tv_message)).check(matches(not(isDisplayed())));
        onView(withText(R.string.error_message)).inRoot(not(isDialog())).check(doesNotExist());
        onView(withId(R.id.storiesView)).check(new RecyclerViewItemCountAssertion(12));
    }

    @Test
    public void normalExecution_RefreshFromMenu() {
        onView(withId(R.id.tv_message)).check(matches(not(isDisplayed())));
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.menu_refresh)).perform(click());
        onView(withText(R.string.error_message)).inRoot(not(isDialog())).check(doesNotExist());
        onView(withId(R.id.storiesView)).check(new RecyclerViewItemCountAssertion(12));
    }

    @Test
    public void openLink_NoErrorMessageShown() {
        onView(withId(R.id.tv_message)).check(matches(not(isDisplayed())));
        onView(withId(R.id.storiesView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void orientationChange_NoErrorMessageShown() {
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.storiesView)).check(new RecyclerViewItemCountAssertion(12));
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
    }
}
