package com.rafsan.classifiedsapp.ui

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import com.rafsan.classifiedsapp.R
import com.rafsan.classifiedsapp.ui.detail.DetailActivity
import com.rafsan.classifiedsapp.ui.main.MainActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    /**
     * Use {@link ActivityScenario} to create and launch the activity under test. This is a
     * replacement for {@link androidx.test.rule.ActivityTestRule}.
     */
    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    lateinit var scenario: ActivityScenario<MainActivity>

    @Test
    fun testRecyclerviewDataCount() {
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity { activityScenarioRule ->
            val recyclerView =
                activityScenarioRule.findViewById<RecyclerView>(R.id.rv_items)
            val itemCount = recyclerView.adapter?.itemCount ?: 0
            assertThat(itemCount).isEqualTo(20)
        }
    }

    @Test
    fun recyclerViewItemClickGoToDetailActivity() {
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.CREATED)
        onView(withId(R.id.rv_items))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        Intents.init();
        intended(hasComponent(DetailActivity::class.java.getName()))
        Intents.release()
    }

    @After
    fun cleanup() {
        scenario.close()
    }
}