package blockchain

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BlockchainTest {
    private lateinit var blockchain: Blockchain
    private lateinit var mempool: Mempool

    @BeforeTest
    fun setup() {
        blockchain = Blockchain()
        mempool = Mempool(blockchain)
    }

    @Test
    fun `chain starts with genesis block`() {
        assertEquals(1, blockchain.chain.size)
        assertEquals(0, blockchain.chain[0].index)
        assertEquals("0", blockchain.chain[0].previousHash)
    }

    @Test
    fun `adding a block increases chain length`() {
        val wallet = Wallet()
        blockchain.addBlock(mutableListOf(), wallet.publicKeyString)
        assertEquals(2, blockchain.chain.size)
    }

    @Test
    fun `each block links to the previous block hash`() {
        val wallet = Wallet()
        blockchain.addBlock(mutableListOf(), wallet.publicKeyString)
        blockchain.addBlock(mutableListOf(), wallet.publicKeyString)

        val chain = blockchain.chain
        for (i in 1 until chain.size) {
            assertEquals(chain[i - 1].hash, chain[i].previousHash)
        }
    }

    @Test
    fun `isValid returns true for untampered chain`() {
        val wallet = Wallet()
        blockchain.addBlock(mutableListOf(), wallet.publicKeyString)
        assertTrue(blockchain.isValid())
    }

    @Test
    fun `isValid returns false after tampering`() {
        val wallet = Wallet()
        blockchain.addBlock(mutableListOf(), wallet.publicKeyString)

        // Replace a block with a tampered previousHash
        val original = blockchain.chain[1]
        blockchain.chain[1] = Block(
            original.index,
            original.timestamp,
            original.transactions,
            "tampered",
            original.nonce
        )
        assertFalse(blockchain.isValid())
    }

    @Test
    fun `miner receives coinbase reward`() {
        val wallet = Wallet()
        blockchain.addBlock(mutableListOf(), wallet.publicKeyString)
        assertEquals(50.0, blockchain.getBalance(wallet.publicKeyString))
    }

    @Test
    fun `getBalance reflects sent and received amounts`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()

        // Mine a block so wallet1 has funds
        blockchain.addBlock(mutableListOf(), wallet1.publicKeyString)

        // Send 10 coins from wallet1 to wallet2
        val tx = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 10.0)
        )
        mempool.submit(tx)
        val pending = mempool.pull(10).toMutableList()
        blockchain.addBlock(pending, wallet2.publicKeyString)

        // wallet1: 50 (mined) - 10 (sent) = 40
        // wallet2: 10 (received) + 50 (mined) = 60
        assertEquals(40.0, blockchain.getBalance(wallet1.publicKeyString))
        assertEquals(60.0, blockchain.getBalance(wallet2.publicKeyString))
    }

    @Test
    fun `insufficient funds transaction is not included in block`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()

        // wallet1 has no funds, try to send
        val tx = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 100.0)
        )

        // submit should reject this
        var rejected = false
        try {
            mempool.submit(tx)
        } catch (e: IllegalArgumentException) {
            rejected = true
        }
        assertTrue(rejected)
    }
}