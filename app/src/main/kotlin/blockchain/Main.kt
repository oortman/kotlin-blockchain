package blockchain

fun main() {
    val wallet1 = Wallet()
    val wallet2 = Wallet()
    val transactions1 = mutableListOf<Transaction>()
    val transactions2 = mutableListOf<Transaction>()

    Blockchain.addBlock(transactions1, wallet1.publicKeyString)

    transactions2.add(Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 2.5))
    transactions2[0] = wallet1.sign(transactions2[0])
    transactions2.add(Transaction(wallet2.publicKeyString, wallet1.publicKeyString, 999.0))
    transactions2[1] = wallet2.sign(transactions2[1])
    Blockchain.addBlock(transactions2, wallet2.publicKeyString)

    Blockchain.chain.forEach { println(it) }
    Blockchain.chain.forEach { println(it.hash) }
    println(Blockchain.isValid())

    println(Blockchain.getBalance(wallet1.publicKeyString))
    println(Blockchain.getBalance(wallet2.publicKeyString))
}
