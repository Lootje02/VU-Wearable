package nl.hva.vuwearable.models

data class Faq(
    val question: String,
    val answer: String,
    var expand : Boolean,
)
