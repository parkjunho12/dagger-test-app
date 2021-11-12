package co.kr.imageapp.jhfactory.ui.search

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import co.kr.imageapp.jhfactory.DataStatus
import co.kr.imageapp.jhfactory.R
import co.kr.imageapp.jhfactory.TestDataRepository
import co.kr.imageapp.jhfactory.TestUtil.dataStatus
import co.kr.imageapp.jhfactory.launchFragmentInHiltContainer
import co.kr.imageapp.jhfactory.ui.main.fragment.search.SearchFragment
import co.kr.imageapp.jhfactory.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import org.junit.After
import javax.inject.Inject


@HiltAndroidTest
class SearchFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private var mIdlingResource: IdlingResource? = null

    @Inject
    lateinit var repository: TestDataRepository

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        launchFragmentInHiltContainer<SearchFragment>(null, R.style.Theme_Imageapp)
        hiltRule.inject()
    }


    @Test
    fun displayCurRecyclerView() {
        dataStatus = DataStatus.Success
        onView(withContentDescription("내 보관함")).check(matches(isDisplayed()))
        onView(withId(R.id.main_tablayout)).check(matches(isDisplayed()))
        onView(withId(R.id.viewpager_container)).check(matches(isDisplayed()))
    }

    @Test
    fun testNoSearchResult() {
        dataStatus = DataStatus.EmptyResponse
        launchFragmentInHiltContainer<SearchFragment>(null, R.style.Theme_Imageapp)
        onView(withId(R.id.recyclerview_search)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        sleep(Toast.LENGTH_SHORT / 2L)
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister()
        }
    }
}