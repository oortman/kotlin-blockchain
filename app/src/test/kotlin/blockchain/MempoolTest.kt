package blockchain

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MempoolTest {
    private lateinit var blockchain: Blockchain
    private lateinit var mempool: Mempool

    @BeforeTest
    fun setup() {
        blockchain = Blockchain()
        mempool = Mempool(blockchain)
    }

    @Test
    fun `submitting a valid signed transaction adds it to pending`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()
        blockchain.addBlock(mutableListOf(), wallet1.publicKeyString)

        val tx = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 5.0)
        )
        mempool.submit(tx)
        assertEquals(1, mempool.pending.size)
    }

    @Test
    fun `submitting an unsigned transaction throws`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()
        blockchain.addBlock(mutableListOf(), wallet1.publicKeyString)

        val tx = Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 5.0)
        assertFailsWith<IllegalArgumentException> {
            mempool.submit(tx)
        }
    }

    @Test
    fun `submitting a transaction with insufficient funds throws`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()

        val tx = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 100.0)
        )
        assertFailsWith<IllegalArgumentException> {
            mempool.submit(tx)
        }
    }

    @Test
    fun `pull returns and removes transactions from queue`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()
        blockchain.addBlock(mutableListOf(), wallet1.publicKeyString)

        val tx1 = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 1.0)
        )
        val tx2 = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 2.0)
        )
        mempool.submit(tx1)
        mempool.submit(tx2)

        val pulled = mempool.pull(1)
        assertEquals(1, pulled.size)
        assertEquals(1.0, pulled[0].amount)
        assertEquals(1, mempool.pending.size)
    }

    @Test
    fun `pull with max greater than pending size does not crash`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()
        blockchain.addBlock(mutableListOf(), wallet1.publicKeyString)

        val tx = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 1.0)
        )
        mempool.submit(tx)

        val pulled = mempool.pull(100)
        assertEquals(1, pulled.size)
        assertTrue(mempool.pending.isEmpty())
    }

    @Test
    fun `pull from empty mempool returns empty list`() {
        val pulled = mempool.pull(10)
        assertTrue(pulled.isEmpty())
    }
}