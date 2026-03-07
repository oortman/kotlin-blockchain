package blockchain

data class Block(
        val index: Int,
        val timestamp: Long,
        val data: String,
        val previousHash: String,
) {
    val hash = computeHash(index, timestamp, data, previousHash)

    companion object {
        fun computeHash(index: Int, timestamp: Long, data: String, previousHash: String): String {
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            val hashByteArray = digest.digest("$index$timestamp$data$previousHash".toByteArray())
            return hashByteArray.joinToString("") { "%02x".format(it) }
        }
    }
}
