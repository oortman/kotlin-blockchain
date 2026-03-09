package blockchain

fun main() {
    val wallet1 = Wallet()
    val wallet2 = Wallet()
    val transactions = mutableListOf<Transaction>()

    Blockchain.addBlock(wallet1.publicKeyString)

    transactions.add(Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 2.5))
    transactions[0] = wallet1.sign(transactions[0])
    Mempool.submit(transactions[0])
    transactions.add(Transaction(wallet2.publicKeyString, wallet1.publicKeyString, 999.0))
    transactions[1] = wallet2.sign(transactions[1])
    try {
        Mempool.submit(transactions[1])
    } catch (e: IllegalArgumentException) {
        println("Transaction rejected: insufficient funds")
    }
    Blockchain.addBlock(wallet2.publicKeyString)

    Blockchain.chain.forEach { println(it) }
    Blockchain.chain.forEach { println(it.hash) }
    println(Blockchain.isValid())

    println(Blockchain.getBalance(wallet1.publicKeyString))
    println(Blockchain.getBalance(wallet2.publicKeyString))
}
