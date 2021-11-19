package com.rafsan.classifiedsapp.ui


import FakeDataUtil
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rafsan.classifiedsapp.R
import com.rafsan.classifiedsapp.data.model.Result
import com.rafsan.classifiedsapp.ui.detail.DetailActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<DetailActivity> =
        ActivityScenarioRule(DetailActivity::class.java)

    lateinit var scenario: ActivityScenario<DetailActivity>

    @Test
    fun testDetailLaunchWithBundle() {
        val fakeItem = FakeDataUtil.getFakeListItem()
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java)
            .putExtra("item", fakeItem)
        scenario = launchActivity(intent)
        onView(withId(R.id.tv_name)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_name)).check(matches(withText("Test1")))
        onView(withId(R.id.tv_price)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_price)).check(matches(withText("20 USD")))
        onView(withId(R.id.tv_created_at)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_created_at)).check(matches(withText("2020-10-08")))
    }

    @Test
    fun testDetailLaunchWithEmptyBundle() {
        val param: Result? = null
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java)
            .putExtra("item", param)
        scenario = launchActivity(intent)
        onView(withId(R.id.tv_name)).check(matches(withText("")))
        onView(withId(R.id.tv_price)).check(matches(withText("")))
        onView(withId(R.id.tv_created_at)).check(matches(withText("")))
    }

    @After
    fun cleanup() {
        scenario.close()
    }
}