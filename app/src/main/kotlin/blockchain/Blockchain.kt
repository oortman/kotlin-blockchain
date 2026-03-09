package blockchain

object Blockchain {
    val chain = mutableListOf<Block>()

    init {
        // Genesis block has no predecessor; "0" is a conventional sentinel
        chain.add(Block(0, System.currentTimeMillis(), emptyList(), "0"))
    }

    fun addBlock(transactions: MutableList<Transaction>, minerAddress: String) {
        transactions.add(Transaction(Transaction.COINBASE, minerAddress, 50.0))

        val validTransactions = transactions.filter { hasSufficientFunds(it) }
        val previousBlock = chain.last()
        val newBlock =
                Block(
                        previousBlock.index + 1,
                        System.currentTimeMillis(),
                        validTransactions,
                        previousBlock.hash
                )

        chain.add(newBlock.mine(4))
    }

    fun getBalance(address: String): Double {
        val allTransactions = chain.flatMap { it.transactions }
        val receiveTransactions = allTransactions.filter { it.recipient == address }
        val sendTransactions = allTransactions.filter { it.sender == address }

        return receiveTransactions.sumOf { it.amount } - sendTransactions.sumOf { it.amount }
    }

    fun hasSufficientFunds(transaction: Transaction): Boolean {
        return (transaction.sender == Transaction.COINBASE) ||
                (getBalance(transaction.sender) >= transaction.amount)
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
        val validSignatures =
                chain.all { block ->
                    block.transactions.all {
                        it.sender == Transaction.COINBASE || Wallet.verify(it)
                    }
                }

        return validHashes && validLinks && validSignatures
    }
}
