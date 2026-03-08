package blockchain

fun main() {
    val wallet1 = Wallet()
    val wallet2 = Wallet()
    val transactions = mutableListOf<Transaction>()

    transactions.add(Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 2.5))
    transactions[0] = wallet1.sign(transactions[0])
    transactions.add(Transaction(wallet2.publicKeyString, wallet1.publicKeyString, 999.0))
    transactions[1] = wallet2.sign(transactions[1])
    Blockchain.addBlock(transactions)

    Blockchain.chain.forEach { println(it) }
    Blockchain.chain.forEach { println(it.hash) }
    println(Blockchain.isValid())
}
