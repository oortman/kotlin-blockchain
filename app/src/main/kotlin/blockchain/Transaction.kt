package blockchain

data class Transaction(
        val sender: String,
        val recipient: String,
        val amount: Double,
        val signature: ByteArray? = null
) {}
