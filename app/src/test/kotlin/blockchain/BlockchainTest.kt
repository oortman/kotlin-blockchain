package blockchain

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BlockchainTest {
    @BeforeTest
    fun setup() {
        Blockchain.reset()
        Mempool.reset()
    }

    @Test
    fun `chain starts with genesis block`() {
        assertEquals(1, Blockchain.chain.size)
        assertEquals(0, Blockchain.chain[0].index)
        assertEquals("0", Blockchain.chain[0].previousHash)
    }

    @Test
    fun `adding a block increases chain length`() {
        val wallet = Wallet()
        Blockchain.addBlock(wallet.publicKeyString)
        assertEquals(2, Blockchain.chain.size)
    }

    @Test
    fun `each block links to the previous block hash`() {
        val wallet = Wallet()
        Blockchain.addBlock(wallet.publicKeyString)
        Blockchain.addBlock(wallet.publicKeyString)

        val chain = Blockchain.chain
        for (i in 1 until chain.size) {
            assertEquals(chain[i - 1].hash, chain[i].previousHash)
        }
    }

    @Test
    fun `isValid returns true for untampered chain`() {
        val wallet = Wallet()
        Blockchain.addBlock(wallet.publicKeyString)
        assertTrue(Blockchain.isValid())
    }

    @Test
    fun `isValid returns false after tampering`() {
        val wallet = Wallet()
        Blockchain.addBlock(wallet.publicKeyString)

        // Replace a block with a tampered previousHash
        val original = Blockchain.chain[1]
        Blockchain.chain[1] = Block(
            original.index,
            original.timestamp,
            original.transactions,
            "tampered",
            original.nonce
        )
        assertFalse(Blockchain.isValid())
    }

    @Test
    fun `miner receives coinbase reward`() {
        val wallet = Wallet()
        Blockchain.addBlock(wallet.publicKeyString)
        assertEquals(50.0, Blockchain.getBalance(wallet.publicKeyString))
    }

    @Test
    fun `getBalance reflects sent and received amounts`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()

        // Mine a block so wallet1 has funds
        Blockchain.addBlock(wallet1.publicKeyString)

        // Send 10 coins from wallet1 to wallet2
        val tx = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 10.0)
        )
        Mempool.submit(tx)
        Blockchain.addBlock(wallet2.publicKeyString)

        // wallet1: 50 (mined) - 10 (sent) = 40
        // wallet2: 10 (received) + 50 (mined) = 60
        assertEquals(40.0, Blockchain.getBalance(wallet1.publicKeyString))
        assertEquals(60.0, Blockchain.getBalance(wallet2.publicKeyString))
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
            Mempool.submit(tx)
        } catch (e: IllegalArgumentException) {
            rejected = true
        }
        assertTrue(rejected)
    }
}
