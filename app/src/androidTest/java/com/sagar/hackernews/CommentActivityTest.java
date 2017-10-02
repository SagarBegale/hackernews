package com.sagar.hackernews;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sagar.hacknews.test.utils.RecyclerViewItemCountAssertion;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
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
public class CommentActivityTest {
    @Rule
    public ActivityTestRule<CommentsActivity> activityRule = new ActivityTestRule<>(CommentsActivity.class, true,
            false);

    @Test
    public void commentsAvailable_NoErrorMessageShown() throws InterruptedException {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), CommentsActivity.class);
        ArrayList<Integer> commentIds = new ArrayList<Integer>();
        commentIds.add(2922097);
        intent.putIntegerArrayListExtra(MainActivity.EXTRA_COMMENT_IDS, commentIds);
        activityRule.launchActivity(intent);
        Thread.sleep(5000);
        onView(withId(R.id.tv_message)).check(matches(not(isDisplayed())));
        onView(withText(R.string.error_message)).inRoot(not(isDialog())).check(doesNotExist());
    }

    @Test
    public void nullCommentId_AppropriateMessageShown() throws InterruptedException {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), CommentsActivity.class);
        intent.putIntegerArrayListExtra(MainActivity.EXTRA_COMMENT_IDS, null);
        activityRule.launchActivity(intent);
        Thread.sleep(5000);
        onView(withId(R.id.tv_message)).check(matches(isDisplayed()));
    }

    @Test
    public void noCommentId_AppropriateMessageShown() throws InterruptedException {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), CommentsActivity.class);

        intent.putIntegerArrayListExtra(MainActivity.EXTRA_COMMENT_IDS, new ArrayList<Integer>());
        activityRule.launchActivity(intent);
        Thread.sleep(5000);
        onView(withId(R.id.tv_message)).check(matches(isDisplayed()));
    }

    @Test
    public void invalidCommandId_AppropriateMessageShown() throws InterruptedException {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), CommentsActivity.class);
        ArrayList<Integer> commentIds = new ArrayList<Integer>();
        commentIds.add(1);
        intent.putIntegerArrayListExtra(MainActivity.EXTRA_COMMENT_IDS, commentIds);
        activityRule.launchActivity(intent);
        Thread.sleep(5000);
        onView(withId(R.id.tv_message)).check(matches(isDisplayed()));
    }
    @Test
    public void orientationChange_NoErrorMessageShown() throws InterruptedException {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), CommentsActivity.class);
        ArrayList<Integer> commentIds = new ArrayList<Integer>();
        commentIds.add(2922097);
        intent.putIntegerArrayListExtra(MainActivity.EXTRA_COMMENT_IDS, commentIds);
        activityRule.launchActivity(intent);
        Thread.sleep(5000);
        onView(withId(R.id.tv_message)).check(matches(not(isDisplayed())));
        onView(withText(R.string.error_message)).inRoot(not(isDialog())).check(doesNotExist());
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.commentsView)).check(new RecyclerViewItemCountAssertion(1));
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
}
