package blockchain

object Mempool {
    val pending: ArrayDeque<Transaction> = ArrayDeque(0)

    fun submit(transaction: Transaction) {
        require(transaction.signature != null)
        require(Wallet.verify(transaction))
        require(Blockchain.hasSufficientFunds(transaction))

        pending.addLast(transaction)
    }

    fun pull(max: Int): List<Transaction> {
        val pulledTransactions = mutableListOf<Transaction>()

        while (pulledTransactions.size < max && pending.isNotEmpty()) {
            pulledTransactions.add(pending.first())
            pending.removeFirst()
        }

        return pulledTransactions
    }

    fun reset() {
        pending.clear()
    }
}
