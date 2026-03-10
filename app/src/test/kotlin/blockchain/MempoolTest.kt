package blockchain

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MempoolTest {
    @BeforeTest
    fun setup() {
        Blockchain.reset()
        Mempool.reset()
    }

    @Test
    fun `submitting a valid signed transaction adds it to pending`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()
        Blockchain.addBlock(wallet1.publicKeyString)

        val tx = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 5.0)
        )
        Mempool.submit(tx)
        assertEquals(1, Mempool.pending.size)
    }

    @Test
    fun `submitting an unsigned transaction throws`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()
        Blockchain.addBlock(wallet1.publicKeyString)

        val tx = Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 5.0)
        assertFailsWith<IllegalArgumentException> {
            Mempool.submit(tx)
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
            Mempool.submit(tx)
        }
    }

    @Test
    fun `pull returns and removes transactions from queue`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()
        Blockchain.addBlock(wallet1.publicKeyString)

        val tx1 = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 1.0)
        )
        val tx2 = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 2.0)
        )
        Mempool.submit(tx1)
        Mempool.submit(tx2)

        val pulled = Mempool.pull(1)
        assertEquals(1, pulled.size)
        assertEquals(1.0, pulled[0].amount)
        assertEquals(1, Mempool.pending.size)
    }

    @Test
    fun `pull with max greater than pending size does not crash`() {
        val wallet1 = Wallet()
        val wallet2 = Wallet()
        Blockchain.addBlock(wallet1.publicKeyString)

        val tx = wallet1.sign(
            Transaction(wallet1.publicKeyString, wallet2.publicKeyString, 1.0)
        )
        Mempool.submit(tx)

        val pulled = Mempool.pull(100)
        assertEquals(1, pulled.size)
        assertTrue(Mempool.pending.isEmpty())
    }

    @Test
    fun `pull from empty mempool returns empty list`() {
        val pulled = Mempool.pull(10)
        assertTrue(pulled.isEmpty())
    }
}
