package blockchain

object Blockchain {
    val chain = mutableListOf<Block>()

    init {
        // Genesis block has no predecessor; "0" is a conventional sentinel
        chain.add(Block(0, System.currentTimeMillis(), emptyList(), "0"))
    }

    fun addBlock(transactions: List<Transaction>) {
        val previous = chain.last()
        val block =
                Block(previous.index + 1, System.currentTimeMillis(), transactions, previous.hash)
        chain.add(block.mine(4))
    }

    fun isValid(): Boolean {
        val validHashes =
                chain.all {
                    it.hash ==
                            Block.computeHash(
                                    it.nonce,
                                    it.index,
                                    it.timestamp,
                                    it.transactions,
                                    it.previousHash
                            )
                }
        val validLinks = chain.zipWithNext().all { it.second.previousHash == it.first.hash }
        val validSignatures = chain.all { block -> block.transactions.all { Wallet.verify(it) } }

        return validHashes && validLinks && validSignatures
    }
}
