package com.example.menupracticaltestapp

import android.app.Instrumentation
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.menupracticaltestapp.screens.login.LoginFragment
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestDetailFragment {
    private lateinit var instrumentation: Instrumentation

    @Before
    fun setUp() {
        instrumentation = InstrumentationRegistry.getInstrumentation()
    }

    @Test
    fun testIfAllElementsHaveTheRequiredVisibility() {
        launchFragmentInContainer<LoginFragment>()

        onView(withId(R.id.loginImage)).check(matches(isDisplayed()))
        onView(withId(R.id.loginTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.loginSubtitle)).check(matches(isDisplayed()))
        onView(withId(R.id.loginEnterEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.loginEmailError)).check(matches(not(isDisplayed())))
        onView(withId(R.id.loginEnterPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.loginPasswordError)).check(matches(not(isDisplayed())))
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
        onView(withId(R.id.loginProgressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testIfEmptyEmailAndPasswordAreEntered() {
        launchFragmentInContainer<LoginFragment>()
        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.loginEmailError)).check(matches(isDisplayed()))
        onView(withId(R.id.loginPasswordError)).check(matches(isDisplayed()))
    }

    @Test
    fun testIfEmptyPasswordIsEntered() {
        val testEmail = "test@test.com"
        launchFragmentInContainer<LoginFragment>()

        onView(withId(R.id.loginEnterEmail))
            .perform(
                typeText(testEmail),
                closeSoftKeyboard()
            )

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.loginEmailError)).check(matches(not(isDisplayed())))
        onView(withId(R.id.loginPasswordError)).check(matches(isDisplayed()))
    }

    @Test
    fun testIfEmptyEmailIsEntered() {
        val testPassword = "password"
        launchFragmentInContainer<LoginFragment>()

        onView(withId(R.id.loginEnterPassword))
            .perform(
                typeText(testPassword),
                closeSoftKeyboard()
            )

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.loginEmailError)).check(matches(isDisplayed()))
        onView(withId(R.id.loginPasswordError)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testIfInvalidEmailIsEntered() {
        val testEmail = "email"
        val testPassword = "password"
        launchFragmentInContainer<LoginFragment>()

        onView(withId(R.id.loginEnterEmail))
            .perform(
                typeText(testEmail),
                closeSoftKeyboard()
            )

        onView(withId(R.id.loginEnterPassword))
            .perform(
                typeText(testPassword),
                closeSoftKeyboard()
            )

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.loginEmailError)).check(matches(isDisplayed()))
        onView(withId(R.id.loginPasswordError)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testIfLoaderISShownAfterEnteringEmailAndPassword() {
        val testEmail = "email@email.com"
        val testPassword = "password"
        launchFragmentInContainer<LoginFragment>()

        onView(withId(R.id.loginEnterEmail))
            .perform(
                typeText(testEmail),
                closeSoftKeyboard()
            )

        onView(withId(R.id.loginEnterPassword))
            .perform(
                typeText(testPassword),
                closeSoftKeyboard()
            )

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.loginEmailError)).check(matches(not(isDisplayed())))
        onView(withId(R.id.loginPasswordError)).check(matches(not(isDisplayed())))
        onView(withId(R.id.loginProgressBar)).check(matches(isDisplayed()))
    }
}