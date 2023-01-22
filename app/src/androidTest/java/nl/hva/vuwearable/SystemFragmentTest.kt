package nl.hva.vuwearable

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.location.GeofencingRequest.InitialTrigger
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test the System fragment
 *
 * @author Bunyamin Duduk
 */
class SystemFragmentTest {

    @get:Rule
    val grantAccessNetworkPermission =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_NETWORK_STATE)

    @get:Rule
    val grantInternetPermission = GrantPermissionRule.grant(android.Manifest.permission.INTERNET)

    @get:Rule
    val grantForegroundServicePermission =
        GrantPermissionRule.grant(android.Manifest.permission.FOREGROUND_SERVICE)

    @get:Rule
    val grantAccessWifiStatePermission =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_WIFI_STATE)

    @get:Rule
    val grantAccessFineLocationPermission =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @get:Rule
    val grantAccessCoarseLocationPermission =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    val grantChangeWifiStatePermission =
        GrantPermissionRule.grant(android.Manifest.permission.CHANGE_WIFI_STATE)

    @Before
    fun setupTest() {
        // Initialise intent
        Intents.init()

        // Launch main activity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Set lifecyle to resumed
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        // Click on the system card
        onView(withId(R.id.cv_system)).perform(click())

        // Release intent after every test before initialising again
        Intents.release()
    }

    @Test
    fun check_if_only_live_data_group_is_showing_1() {
        // Check if the live data group only shows
        onView(withId(R.id.live_data_group)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.measurement_group)).check(matches(withEffectiveVisibility((Visibility.INVISIBLE))))
        onView(withId(R.id.shutdown_group)).check(matches(withEffectiveVisibility((Visibility.INVISIBLE))))
    }

    @Test
    fun check_if_all_groups_are_showing_2() {
        // Login
        onView(withId(R.id.logout_button)).perform(click())
        onView(withId(R.id.input_password)).perform(typeText("nextgen2022"))
        onView(withId(R.id.login_button)).perform(click())

        // Check if all the groups are visible
        onView(withId(R.id.live_data_group)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.measurement_group)).check(matches(withEffectiveVisibility((Visibility.VISIBLE))))
        onView(withId(R.id.shutdown_group)).check(matches(withEffectiveVisibility((Visibility.VISIBLE))))
    }

    @Test
    fun check_if_logging_out_hides_all_but_live_data_group_3() {
        // Login and test if everything shows before we logout.
        // Calling this so we don't have duplicate code.
        check_if_all_groups_are_showing_2()

        // Logout
        onView(withId(R.id.logout_button)).perform(click())
        onView(withId(R.id.logout_button)).perform(click())

        // Logging out redirects back to dashboard so we need to go back to system fragment
        onView(withId(R.id.cv_system)).perform(click())

        // Check if only live data group is showing
        check_if_only_live_data_group_is_showing_1()
    }
}