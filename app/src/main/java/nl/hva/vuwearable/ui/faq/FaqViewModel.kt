package nl.hva.vuwearable.ui.faq

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.hva.vuwearable.models.Faq

class FaqViewModel(application: Application) : AndroidViewModel(application) {

    var faqList : MutableList<Faq> = mutableListOf()


    fun getFaqQuestionsAndAnswers() {
        val questions = listOf(
            "Can I shower with the device?",
            "How do I check if the device is working properly?",
            "How do I charge the device?",
        )

        val answers = listOf(
            "Please remove the device and stickers before showering. Dry yourself off well and refrain from applying any lotion. Then reapply the device according to the following instructions (link to step by step (screen by screen) instructions how to clean the skin with alcohol, place the stickers, apply device and cables)",
            "The LED on the device blinks every 3 seconds. The status in the app says ‘data acquisition in progress’.",
            "Place it in the charger (image of how to put it in the charger). A full charge from empty typically takes 2-3 hours. The LED will show charging is completed by turning solid green.",
        )

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