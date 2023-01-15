package nl.hva.vuwearable.ui.faq

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import nl.hva.vuwearable.models.Faq

/**
 * This is the viewmodel of the faq questions and answers
 * In this viewmodel you receive the different questions and answers
 * If you want to add new questions to the faq list, than it needs to be here
 * @author Lorenzo Bindemann
 */
class FaqViewModel(application: Application) : AndroidViewModel(application) {

    var faqList : MutableList<Faq> = mutableListOf()

    /**
     * This function creates a list of different questions and answers based on you login status
     * If you want to add a new questions, than you need to always add an answer aswell
     * @author Lorenzo Bindemann
     */
    fun getFaqQuestionsAndAnswers(userIsLoggedIn : Boolean) {
        val questions: List<String>
        val answers: List<String>

        // List of questions and answers if you are not logged in (participant)
        if (!userIsLoggedIn) {

            // Questions if you are not logged in (participant)
            questions = listOf(
                "Can I shower with the device?",
                "How do I check if the device is working properly?",
                "How do I charge the device?",
            )

            // Answers if you are not logged in (participant)
            answers = listOf(
                "Please remove the device and stickers before showering. Dry yourself off well and refrain from applying any lotion. Then reapply the device according to the following instructions (link to step by step (screen by screen) instructions how to clean the skin with alcohol, place the stickers, apply device and cables)",
                "The LED on the device blinks every 3 seconds. The status in the app says ‘data acquisition in progress’.",
                "Place it in the charger (image of how to put it in the charger). A full charge from empty typically takes 2-3 hours. The LED will show charging is completed by turning solid green.",
            )
        } else {
            // Questions if you are logged in (researcher)
            questions = listOf(
                "How do I check the signal quality and see if it is sufficient?",
                "How do I manage what the participant sees in the dashboard?",
                "How do I check if the device is working properly?",
                "How do I charge the device?"
            )

            // Answers if you are not logged in (researcher)
            answers = listOf(
                "Follow our signal quality wizard after connecting your participant [Start signal quality check] -> takes the researcher through different signal visualisations and does automated checks on the signal plus gives tooltips on what the researcher should look for",
                "-",
                "The LED on the device blinks every 3 seconds. The status in the app says ‘data acquisition in progress’.",
                "Place it in the charger (image of how to put it in the charger). A full charge from empty typically takes 2-3 hours. The LED will show charging is completed by turning solid green.",
            )
        }

        // If you come twice on the same screen, the list needs to be made empty first
        faqList = mutableListOf()

        // Looping through the different lists to add them to the list
        for (i in questions.indices) {
            val faq = Faq(
                questions[i],
                answers[i],
                false,
            )
            faqList.add(faq)
        }
    }
}