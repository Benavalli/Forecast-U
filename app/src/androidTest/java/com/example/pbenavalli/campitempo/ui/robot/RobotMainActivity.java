package com.example.pbenavalli.campitempo.ui.robot;

import android.content.res.Resources;

import com.example.pbenavalli.campitempo.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by pbenavalli on 5/16/17.
 */

public class RobotMainActivity {

    Resources resources;

    public RobotMainActivity(Resources resources) {
        this.resources = resources;
    }

    public RobotMainActivity verifyElementInHeader() {
        onView(withId(R.id.list_item_date_textview));
        onView(withText(R.string.test_expresso));
        onView(allOf(withId(R.id.list_item_date_textview), withText(R.string.test_expresso)));
        onView(anyOf(withId(R.id.list_item_date_textview), withText(R.string.test_expresso)));
        onView(allOf(withId(R.id.list_item_date_textview), not(withText(R.string.test_expresso_not))));
        return this;
    }

    public RobotMainActivity verifyResources() {
        assertThat("Rain", allOf(startsWith("R"), containsString(resources.getString(R.string.test_expresso_part))));
        assertThat(resources.getString(R.string.test_expresso_rain), anyOf(startsWith("R"), containsString(resources.getString(R.string.test_expresso_part))));
        return this;
    }

    public RobotMainActivity verifyMatchesDisplayed() {
        onView(withId(R.id.list_item_low_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.list_item_high_textview)).check(matches(isDisplayed()));
        return this;
    }

    public RobotMainActivity scroll() {
        onView(withId(R.id.list_item_date_textview)).perform(swipeUp());
        return this;
    }

    public RobotMainActivity verifyAdapter() {
        onData(anything())
                .inAdapterView(withId(R.id.listview_forecast))
                .atPosition(2)
                .check(matches(isDisplayed()))
                .perform(click());
        return this;
    }
}
