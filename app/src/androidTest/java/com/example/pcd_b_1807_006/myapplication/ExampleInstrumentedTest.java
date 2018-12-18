package com.example.pcd_b_1807_006.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest  extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;

    public ExampleInstrumentedTest() {
        super(MainActivity.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void test1() {
        MainActivity activity = getActivity();
        Assert.assertNotNull(activity);

    }

    @Test
    public void login_and_listDisplayed(){
        // 로그인 버튼 클릭.
        onView(withId(R.id.test)).perform(ViewActions.click());
// 로그인에 힌트가 보이는지 확인.
        onView(withId(R.id.test)).check(ViewAssertions.matches(withHint(R.string.app_name)));

    }




    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.pcd_b_1807_006.myapplication", appContext.getPackageName());
    }
}