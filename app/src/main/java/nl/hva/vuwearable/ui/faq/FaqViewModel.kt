package nl.hva.vuwearable.ui.faq

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import nl.hva.vuwearable.R
import nl.hva.vuwearable.models.Faq

/**
 * This is the viewmodel of the faq questions and answers
 * In this viewmodel you receive the different questions and answers
 * If you want to add new questions to the faq list, than it needs to be here
 * @author Lorenzo Bindemann
 */
class FaqViewModel(application: Application) : AndroidViewModel(application) {

    val APPLICATION = application
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
                APPLICATION.getString(R.string.participant_question_1),
                APPLICATION.getString(R.string.participant_question_2),
                APPLICATION.getString(R.string.participant_question_3),
            )

            // Answers if you are not logged in (participant)
            answers = listOf(
                APPLICATION.getString(R.string.participant_answer_1),
                APPLICATION.getString(R.string.participant_answer_2),
                APPLICATION.getString(R.string.participant_answer_3),
            )
        } else {
            // Questions if you are logged in (researcher)
            questions = listOf(
                APPLICATION.getString(R.string.researcher_question_1),
                APPLICATION.getString(R.string.researcher_question_2),
                APPLICATION.getString(R.string.researcher_question_3),
            )

            // Answers if you are not logged in (researcher)
            answers = listOf(
                APPLICATION.getString(R.string.researcher_answer_1),
                APPLICATION.getString(R.string.researcher_answer_2),
                APPLICATION.getString(R.string.researcher_answer_3),
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