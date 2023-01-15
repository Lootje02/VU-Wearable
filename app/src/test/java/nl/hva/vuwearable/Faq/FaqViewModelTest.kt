package nl.hva.vuwearable.Faq

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import android.app.Application
import junit.framework.Assert.assertEquals
import nl.hva.vuwearable.ui.faq.FaqViewModel

@RunWith(MockitoJUnitRunner::class)
class FaqViewModelTest {

    @Mock //Annotation to create a mock object of the Application class
    lateinit var application: Application
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
        assertEquals(3, faqViewModel.faqList.size)
        assertEquals("Can I shower with the device?", faqViewModel.faqList[0].question)
        assertEquals("Please remove the device and stickers before showering. Dry yourself off well and refrain from applying any lotion. Then reapply the device according to the following instructions (link to step by step (screen by screen) instructions how to clean the skin with alcohol, place the stickers, apply device and cables)", faqViewModel.faqList[0].answer)
    }

    @Test
    fun testGetFaqQuestionsAndAnswersWithUserLoggedIn() {
        // calling the getFaqQuestionsAndAnswers method with userLoggedIn as true
        faqViewModel.getFaqQuestionsAndAnswers(true)
        assertEquals(4, faqViewModel.faqList.size)
        assertEquals("How do I check the signal quality and see if it is sufficient?", faqViewModel.faqList[0].question)
        assertEquals("Follow our signal quality wizard after connecting your participant [Start signal quality check] -> takes the researcher through different signal visualisations and does automated checks on the signal plus gives tooltips on what the researcher should look for", faqViewModel.faqList[0].answer)
    }
}