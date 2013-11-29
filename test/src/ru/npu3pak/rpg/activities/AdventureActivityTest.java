package ru.npu3pak.rpg.activities;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class ru.npu3pak.rpg.activities.AdventureActivityTest \
 * ru.npu3pak.rpg.tests/android.test.InstrumentationTestRunner
 */
public class AdventureActivityTest extends ActivityInstrumentationTestCase2<AdventureActivity> {

    public AdventureActivityTest() {
        super("ru.npu3pak.rpg", AdventureActivity.class);
    }

}
