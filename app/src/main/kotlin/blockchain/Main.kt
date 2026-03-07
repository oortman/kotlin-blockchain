package blockchain

fun main() {
    Blockchain.addBlock("First Block")
    Blockchain.addBlock("Second Block")

    Blockchain.chain.forEach { println(it) }
    Blockchain.chain.forEach { println(it.hash) }
}
