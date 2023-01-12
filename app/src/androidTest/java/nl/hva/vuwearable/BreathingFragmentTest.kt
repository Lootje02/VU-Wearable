package nl.hva.vuwearable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import nl.hva.vuwearable.databinding.FragmentBreathingSetupBinding
import nl.hva.vuwearable.ui.breathing.BreathingFragment
import nl.hva.vuwearable.ui.breathing.BreathingViewModel
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BreathingFragmentTest {

    private val fragment = BreathingFragment()

    private var _binding: FragmentBreathingSetupBinding? = null

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val binding get() = _binding!!

    private val viewModel = BreathingViewModel()

    @Before
    fun setUp() {
        _binding = FragmentBreathingSetupBinding.bind(View)
        fragment.onCreateView(LayoutInflater.from(context), null, null)
    }

    @Test
    fun testSeekBarValues() {
        // Launch the fragment
        launchFragmentInContainer<BreathingFragment>()

        // Set the progress of the seekbar
        onView(withId(binding.seekbarBreatheIn.id)).perform(setProgress(2))
        onView(withId(binding.seekbarBreatheOut.id)).perform(setProgress(3))
        onView(withId(binding.seekbarDuration.id)).perform(setProgress(10))

        // Check if the progress of the seekbar is correct
        onView(withId(binding.seekbarBreatheIn.id)).check(matches(withProgress(2)))
        onView(withId(binding.seekbarBreatheOut.id)).check(matches(withProgress(3)))
        onView(withId(binding.seekbarDuration.id)).check(matches(withProgress(10)))

        // Check if the value of breatheIn in the ViewModel is correct
        onView(withId(binding.btnStartExcercise.id)).perform(click())
        assertEquals(2, viewModel.breatheIn.value)
        assertEquals(3, viewModel.breatheOut.value)
        assertEquals(10, viewModel.maxDuration.value)
    }
}

fun withProgress(progress: Int): Matcher<View> {
    return object : BoundedMatcher<View, SeekBar>(SeekBar::class.java) {
        override fun matchesSafely(seekBar: SeekBar): Boolean {
            return seekBar.progress == progress
        }

        override fun describeTo(description: Description) {
            description.appendText("with progress: ")
            description.appendValue(progress)
        }
    }
}

fun setProgress(progress: Int): ViewAction? {
    return object : ViewAction {
        override fun perform(uiController: UiController?, view: View?) {
            val seekBar = view as SeekBar
            seekBar.progress = progress
        }

        override fun getDescription(): String {
            return "Set a progress on a SeekBar"
        }

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(SeekBar::class.java)
        }
    }
}










