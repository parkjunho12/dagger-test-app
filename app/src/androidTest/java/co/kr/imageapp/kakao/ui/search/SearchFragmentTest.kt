package co.kr.imageapp.kakao.ui.search

import android.view.KeyEvent
import android.widget.AutoCompleteTextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import co.kr.imageapp.kakao.DataStatus
import co.kr.imageapp.kakao.R
import co.kr.imageapp.kakao.TestDataRepository
import co.kr.imageapp.kakao.TestUtil.dataStatus
import co.kr.imageapp.kakao.launchFragmentInHiltContainer
import co.kr.imageapp.kakao.ui.main.MainActivity
import co.kr.imageapp.kakao.ui.main.fragment.search.SearchFragment
import co.kr.imageapp.kakao.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import org.mockito.Mockito.mock
import org.hamcrest.Matchers.not
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