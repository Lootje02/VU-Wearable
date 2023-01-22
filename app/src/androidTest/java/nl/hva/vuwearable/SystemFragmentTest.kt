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
        Intents.init()

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        activityScenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.cv_system)).perform(click())
        Intents.release()
    }

    @Test
    fun check_if_only_live_data_group_is_showing() {
        onView(withId(R.id.live_data_group)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.measurement_group)).check(matches(withEffectiveVisibility((Visibility.INVISIBLE))))
        onView(withId(R.id.shutdown_group)).check(matches(withEffectiveVisibility((Visibility.INVISIBLE))))
    }

    @Test
    fun testSystemFragment() {
        onView(withId(R.id.logout_button)).perform(click())
        onView(withId(R.id.input_password)).perform(typeText("nextgen2022"))
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.live_data_group)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.measurement_group)).check(matches(withEffectiveVisibility((Visibility.VISIBLE))))
        onView(withId(R.id.shutdown_group)).check(matches(withEffectiveVisibility((Visibility.VISIBLE))))
    }
}