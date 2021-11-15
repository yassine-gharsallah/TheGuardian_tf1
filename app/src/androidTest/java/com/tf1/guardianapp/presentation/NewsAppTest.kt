package com.tf1.guardianapp.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.tf1.guardianapp.R
import com.tf1.guardianapp.ui.MainActivity
import com.tf1.guardianapp.ui.articledetail.ArticleDetailFragment
import org.junit.After
import org.junit.Before
import org.junit.Rule

@LargeTest
class NewsAppTest {

    companion object {
        const val DELAY_START: Long = 4000
        const val DELAY_ON_CHANGE_SCREEN: Long = 2000
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    /**
     * Test click on List and redirection to details and check data well set
     */
    //@Test
    fun checkTransmissionAndDataValid() {
        Thread.sleep(DELAY_START)
        onView(withId(R.id.articles_recyclerview))
            .perform(click())

        // Email screen
        Thread.sleep(DELAY_ON_CHANGE_SCREEN)
        intended(hasComponent(ArticleDetailFragment::class.java.name))

        onView(withId(R.id.article_image))
            .check(matches(isDisplayed()))
            .perform(click())

    }
}
