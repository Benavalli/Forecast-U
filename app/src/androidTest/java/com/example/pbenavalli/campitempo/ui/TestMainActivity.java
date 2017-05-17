package com.example.pbenavalli.campitempo.ui;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.pbenavalli.campitempo.MainActivity;
import com.example.pbenavalli.campitempo.ui.base.BaseTest;
import com.example.pbenavalli.campitempo.ui.robot.RobotMainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by pbenavalli on 5/16/17.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestMainActivity extends BaseTest{

    RobotMainActivity robot;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        robot = new RobotMainActivity(mActivityRule.getActivity().getResources());
    }

    @Test
    public void testHeaderElements() {
        robot
                .verifyElementInHeader()
                .verifyResources();

        super.doWait();
    }

    @Test
    public void testMatchesDisplayed() {
        robot
                .verifyMatchesDisplayed();
    }

    /*@Test
    public void testSwipe() {
        robot
                .scroll();
        doWait();

    }*/

    @Test
    public void testAdapter() {
        robot
                .verifyAdapter();
        super.doWait();
    }

    @Override
    public void doWait() {
        super.doWait(3000l);
    }
}
