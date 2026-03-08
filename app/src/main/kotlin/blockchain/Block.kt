package blockchain

import java.security.MessageDigest

data class Block(
        val index: Int,
        val timestamp: Long,
        val transactions: List<Transaction>,
        val previousHash: String,
        val nonce: Int = 0
) {
    val hash = computeHash(nonce, index, timestamp, transactions, previousHash)

    companion object {
        fun computeHash(
                nonce: Int,
                index: Int,
                timestamp: Long,
                transactions: List<Transaction>,
                previousHash: String
        ): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val transactionsString =
                    transactions.joinToString("") { "${it.sender}${it.recipient}${it.amount}" }
            val hashByteArray =
                    digest.digest(
                            "$nonce$index$timestamp$transactionsString$previousHash".toByteArray()
                    )
            return hashByteArray.joinToString("") { "%02x".format(it) }
        }
    }

    fun mine(difficulty: Int): Block {
        var block = this
        val target = "0".repeat(difficulty)

        while (!block.hash.startsWith(target)) {
            block = block.copy(nonce = block.nonce + 1)
        }

        return block
    }
}
