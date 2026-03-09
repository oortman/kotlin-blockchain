package blockchain

data class Transaction(
        val sender: String,
        val recipient: String,
        val amount: Double,
        val signature: ByteArray? = null
) {
    companion object {
        const val COINBASE = "COINBASE"
    }
}
