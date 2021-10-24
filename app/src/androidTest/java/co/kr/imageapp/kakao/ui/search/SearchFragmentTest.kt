package co.kr.imageapp.kakao.ui.search

import android.view.KeyEvent
import android.widget.AutoCompleteTextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import co.kr.imageapp.kakao.DataStatus
import co.kr.imageapp.kakao.R
import co.kr.imageapp.kakao.TestUtil.dataStatus
import co.kr.imageapp.kakao.ui.main.fragment.search.SearchFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import org.mockito.Mockito.mock


@HiltAndroidTest
class SearchFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {

    }

    @Test
    fun testNoSearchResult() {
        dataStatus = DataStatus.EmptyResponse
        val searchText = "도깨비입니다요오오"

        launchFragmentInContainer() {
            SearchFragment()
        }
        onView(withId(R.id.search_bar)).perform(click())
        sleep(LENGTH_LONG * 2L)
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText(searchText))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        sleep(Toast.LENGTH_SHORT / 2L)
        onView(withText(R.string.search_errors))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSearch() {
        dataStatus = DataStatus.Success
        val scenario = launchFragmentInContainer<SearchFragment>()
        onView(withId(R.id.search_bar)).perform(click())
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText("도깨비"))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
//        onView(withId(R.id.tv_name)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_description)).perform(scrollTo())
//        onView(withId(R.id.tv_description)).check(matches(isDisplayed()))
    }

}