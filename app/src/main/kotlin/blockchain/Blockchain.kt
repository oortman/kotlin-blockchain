package blockchain

object Blockchain {
    val chain = mutableListOf<Block>()

    init {
        chain.add(Block(0, System.currentTimeMillis(), "Genesis Block", "0"))
    }

    fun addBlock(data: String) {
        val previous = chain.last()
        val block = Block(previous.index + 1, System.currentTimeMillis(), data, previous.hash)
        chain.add(block.mine(5))
    }

    fun isValid(): Boolean {
        val validHashes = chain.all { it.hash == Block.computeHash(it.nonce, it.index, it.timestamp, it.data, it.previousHash) }
        val validLinks = chain.zipWithNext().all { it.second.previousHash == it.first.hash }

        return validHashes && validLinks
    }
}
