package com.rafsan.classifiedsapp.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rafsan.classifiedsapp.R
import com.rafsan.classifiedsapp.ui.detail.DetailActivity
import com.rafsan.classifiedsapp.ui.main.MainActivity
import com.rafsan.classifiedsapp.util.EspressoIdlingResourceRule
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Before
    fun setUp() {
        Intents.init();
    }

    @Test
    fun test_isListItemsVisibleOnAppLaunch() {
        onView(withId(R.id.rv_items)).check(matches(isDisplayed()))

        onView(withId(R.id.progressBar)).check(matches(CoreMatchers.not(isDisplayed())))
    }

    @Test
    fun test_selectListItem_isDetailActivityVisible() {
        onView(withId(R.id.rv_items))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    ViewActions.click()
                )
            )

        // Confirm nav to DetailActivity
        intended(hasComponent(DetailActivity::class.java.getName()))
        Intents.release()
    }

    @After
    fun cleanup() {

    }
}