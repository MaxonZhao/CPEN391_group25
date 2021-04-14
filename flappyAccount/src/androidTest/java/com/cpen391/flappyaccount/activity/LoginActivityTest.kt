package com.cpen391.flappyaccount.activity


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.cpen391.flappyaccount.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Test
    fun loginActivityTest() {
        val materialButton = onView(
                allOf(
                        withId(R.id.signup_btn), withText("SIGN UP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        4
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        materialButton.perform(click())

        val textInputEditText = onView(
                allOf(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fullName),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText.perform(replaceText("test"), closeSoftKeyboard())

        val textInputEditText2 = onView(
                allOf(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.username),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText2.perform(replaceText("test"), closeSoftKeyboard())

        val textInputEditText3 = onView(
                allOf(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText3.perform(replaceText("test@gammil.co"), closeSoftKeyboard())

        val textInputEditText4 = onView(
                allOf(
                        withText("test@gammil.co"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText4.perform(click())

        val textInputEditText5 = onView(
                allOf(
                        withText("test@gammil.co"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText5.perform(replaceText("test@gmil.co"))

        val textInputEditText6 = onView(
                allOf(
                        withText("test@gmil.co"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText6.perform(closeSoftKeyboard())

        val textInputEditText7 = onView(
                allOf(
                        withText("test@gmil.co"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText7.perform(click())

        val textInputEditText8 = onView(
                allOf(
                        withText("test@gmil.co"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText8.perform(replaceText("test@gmail.co"))

        val textInputEditText9 = onView(
                allOf(
                        withText("test@gmail.co"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText9.perform(closeSoftKeyboard())

        val textInputEditText10 = onView(
                allOf(
                        withText("test@gmail.co"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText10.perform(click())

        val textInputEditText11 = onView(
                allOf(
                        withText("test@gmail.co"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText11.perform(replaceText("test@gmail.com"))

        val textInputEditText12 = onView(
                allOf(
                        withText("test@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.email),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText12.perform(closeSoftKeyboard())

        val relativeLayout = onView(
                allOf(
                        withId(R.id.rlClickConsumer),
                        childAtPosition(
                                allOf(
                                        withId(R.id.countryCodeHolder),
                                        childAtPosition(
                                                withId(R.id.country_code),
                                                0
                                        )
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        relativeLayout.perform(click())

        val appCompatEditText = onView(
                allOf(
                        withId(R.id.editText_search),
                        childAtPosition(
                                allOf(
                                        withId(R.id.rl_query_holder),
                                        childAtPosition(
                                                withId(R.id.rl_holder),
                                                1
                                        )
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        appCompatEditText.perform(replaceText("ca"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
                allOf(
                        withId(R.id.editText_search), withText("ca"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.rl_query_holder),
                                        childAtPosition(
                                                withId(R.id.rl_holder),
                                                1
                                        )
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        appCompatEditText2.perform(click())

        val recyclerView = onView(
                allOf(
                        withId(R.id.recycler_countryDialog),
                        childAtPosition(
                                withId(R.id.rl_holder),
                                2
                        )
                )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(4, click()))

        val textInputEditText13 = onView(
                allOf(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.phoneNo),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText13.perform(replaceText("2368661418"), closeSoftKeyboard())

        val textInputEditText14 = onView(
                allOf(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.password),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText14.perform(replaceText("test1234@"), closeSoftKeyboard())

        val checkableImageButton = onView(
                allOf(
                        withId(R.id.text_input_end_icon), withContentDescription("Show password"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        1
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        checkableImageButton.perform(click())

        val materialButton2 = onView(
                allOf(
                        withId(R.id.register_button), withText("REGISTER"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0
                                ),
                                3
                        ),
                        isDisplayed()
                )
        )
        materialButton2.perform(click())

        val textInputEditText15 = onView(
                allOf(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.username),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText15.perform(replaceText("test"), closeSoftKeyboard())

        val textInputEditText16 = onView(
                allOf(
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.password),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        textInputEditText16.perform(replaceText("test1234@"), closeSoftKeyboard())

        val materialButton3 = onView(
                allOf(
                        withId(R.id.login_btn), withText("GO"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.LinearLayout")),
                                                3
                                        )
                                ),
                                3
                        ),
                        isDisplayed()
                )
        )
        materialButton3.perform(click())

        val materialRadioButton = onView(
                allOf(
                        withId(R.id.tapping_btn), withText("Tapping"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.radio_btn),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.LinearLayout")),
                                                0
                                        )
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        materialRadioButton.perform(click())

        val appCompatSpinner = onView(
                allOf(
                        withId(R.id.select_bird_color),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        5
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        appCompatSpinner.perform(click())



        val materialRadioButton2 = onView(
                allOf(
                        withId(R.id.medium_mode), withText("Medium"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.diff_level_radio_btn),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.LinearLayout")),
                                                0
                                        )
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        materialRadioButton2.perform(click())

        val materialButton4 = onView(
                allOf(
                        withId(R.id.start_yes), withText("YES"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0
                                ),
                                9
                        ),
                        isDisplayed()
                )
        )
        materialButton4.perform(click())

        val materialButton5 = onView(
                allOf(
                        withId(R.id.btnDiscoverable_on_off), withText("Enable Discoverable"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        1
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        materialButton5.perform(click())

        val materialButton6 = onView(
                allOf(
                        withId(R.id.btnFindUnpairedDevices), withText("Discover"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        1
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        materialButton6.perform(click())

        val linearLayout = onData(anything())
            .inAdapterView(
                    allOf(
                            withId(R.id.paired_devices_view),
                            childAtPosition(
                                    withClassName(`is`("android.widget.LinearLayout")),
                                    5
                            )
                    )
            )
            .atPosition(1)
        linearLayout.perform(click())

        val materialButton7 = onView(
                allOf(
                        withId(R.id.btnStartConnection), withText("Connect device"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        1
                                ),
                                6
                        ),
                        isDisplayed()
                )
        )
        materialButton7.perform(click())

        pressBack()

        val appCompatImageView = onView(
                allOf(
                        withId(R.id.profile_icon),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        appCompatImageView.perform(click())

        pressBack()

        pressBack()

        val materialButton8 = onView(
                allOf(
                        withId(R.id.guest_btn), withText("Guest Mode"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        4
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        materialButton8.perform(click())

        val appCompatImageView2 = onView(
                allOf(
                        withId(R.id.profile_icon),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0
                                ),
                                0
                        ),
                        isDisplayed()
                )
        )
        appCompatImageView2.perform(click())

        pressBack()

        val materialRadioButton3 = onView(
                allOf(
                        withId(R.id.tapping_btn), withText("Tapping"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.radio_btn),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.LinearLayout")),
                                                0
                                        )
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        materialRadioButton3.perform(click())

        val appCompatSpinner2 = onView(
                allOf(
                        withId(R.id.select_bird_color),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        5
                                ),
                                1
                        ),
                        isDisplayed()
                )
        )
        appCompatSpinner2.perform(click())


        val materialRadioButton4 = onView(
                allOf(
                        withId(R.id.hard_mode), withText("Hard"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.diff_level_radio_btn),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.LinearLayout")),
                                                0
                                        )
                                ),
                                2
                        ),
                        isDisplayed()
                )
        )
        materialRadioButton4.perform(click())

        val materialButton9 = onView(
                allOf(
                        withId(R.id.start_yes), withText("YES"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0
                                ),
                                9
                        ),
                        isDisplayed()
                )
        )
        materialButton9.perform(click())

        pressBack()

        pressBack()

        val materialButton10 = onView(
                allOf(
                        withId(R.id.reset_btn), withText("Forget Password?"),
                        childAtPosition(
                                allOf(
                                        withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.LinearLayout")),
                                                3
                                        )
                                ),
                                2
                        ),
                        isDisplayed()
                )
        )
        materialButton10.perform(click())

        pressBack()

        val materialButton11 = onView(
                allOf(
                        withId(R.id.next), withText("NEXT"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.ScrollView")),
                                        0
                                ),
                                4
                        )
                )
        )
        materialButton11.perform(scrollTo(), click())

        pressBack()

        pressBack()

        pressBack()
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
