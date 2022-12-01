package nl.hva.vuwearable.models

data class Measurement(
    val totalBytes: Int,
    val title: String,
    val formula: ((Double) -> Double)? = null
) {
    var value = 0.0
}
