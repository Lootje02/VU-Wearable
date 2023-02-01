package nl.hva.vuwearable.Faq

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import nl.hva.vuwearable.ui.faq.FaqViewModel
import org.junit.Rule
import org.mockito.kotlin.mock

/**
 * @author Lorenzo Bindemann
 */
@RunWith(MockitoJUnitRunner::class)
class FaqViewModelTest {

    //Rule used for testing LiveData
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val application = mock<Application>()
    lateinit var faqViewModel: FaqViewModel

    @Before
    fun setUp() {
        // initializing the FaqViewModel class
        faqViewModel = FaqViewModel(application)
    }

    @Test
    fun testGetFaqQuestionsAndAnswersWithUserNotLoggedIn() {
        // calling the getFaqQuestionsAndAnswers method with userNotLoggedIn as false
        faqViewModel.getFaqQuestionsAndAnswers(false)
        Assert.assertEquals(3, faqViewModel.faqList.size)
        Assert.assertEquals("Can I shower with the device?", faqViewModel.faqList[0].question)
        Assert.assertEquals("Please remove the device and stickers before showering. Dry yourself off well and refrain from applying any lotion. Then reapply the device according to the following instructions (link to step by step (screen by screen) instructions how to clean the skin with alcohol, place the stickers, apply device and cables)", faqViewModel.faqList[0].answer)
    }

    @Test
    fun testGetFaqQuestionsAndAnswersWithUserLoggedIn() {
        // calling the getFaqQuestionsAndAnswers method with userLoggedIn as true
        faqViewModel.getFaqQuestionsAndAnswers(true)
        Assert.assertEquals(4, faqViewModel.faqList.size)
        Assert.assertEquals("How do I check the signal quality and see if it is sufficient?", faqViewModel.faqList[0].question)
        Assert.assertEquals("Follow our signal quality wizard after connecting your participant [Start signal quality check] -> takes the researcher through different signal visualisations and does automated checks on the signal plus gives tooltips on what the researcher should look for", faqViewModel.faqList[0].answer)
    }
}