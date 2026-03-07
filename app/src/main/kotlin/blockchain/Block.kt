package blockchain

data class Block(
        val index: Int,
        val timestamp: Long,
        val data: String,
        val previousHash: String,
        val nonce: Int = 0
) {
    val hash = computeHash(nonce, index, timestamp, data, previousHash)

    companion object {
        fun computeHash(nonce: Int, index: Int, timestamp: Long, data: String, previousHash: String): String {
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            val hashByteArray = digest.digest("$nonce$index$timestamp$data$previousHash".toByteArray())
            return hashByteArray.joinToString("") { "%02x".format(it) }
        }
    }

    fun mine(difficulty: Int): Block {
        var block = this
        val target = "0".repeat(difficulty)
        
        while(!block.hash.startsWith(target)) {
            block = block.copy(nonce = block.nonce + 1)
        }

        return block
    }
}
