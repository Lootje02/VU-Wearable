package nl.hva.vuwearable.models

data class Measurement(
    val totalBytes: Int,
    val title: String,
    val formula: ((Long) -> Double)? = null
) {
    var value = 0L
}
