package blockchain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BlockTest {
    @Test
    fun `hash is 64 hex characters`() {
        val block = Block(0, 1000L, emptyList(), "0")
        assertEquals(64, block.hash.length)
        assertTrue(block.hash.all { it in '0'..'9' || it in 'a'..'f' })
    }

    @Test
    fun `same inputs produce the same hash`() {
        val block1 = Block(0, 1000L, emptyList(), "0")
        val block2 = Block(0, 1000L, emptyList(), "0")
        assertEquals(block1.hash, block2.hash)
    }

    @Test
    fun `different data produces different hash`() {
        val tx1 = listOf(Transaction("alice", "bob", 10.0))
        val tx2 = listOf(Transaction("alice", "bob", 20.0))
        val block1 = Block(0, 1000L, tx1, "0")
        val block2 = Block(0, 1000L, tx2, "0")
        assertNotEquals(block1.hash, block2.hash)
    }

    @Test
    fun `different index produces different hash`() {
        val block1 = Block(0, 1000L, emptyList(), "0")
        val block2 = Block(1, 1000L, emptyList(), "0")
        assertNotEquals(block1.hash, block2.hash)
    }

    @Test
    fun `different previousHash produces different hash`() {
        val block1 = Block(0, 1000L, emptyList(), "0")
        val block2 = Block(0, 1000L, emptyList(), "abc")
        assertNotEquals(block1.hash, block2.hash)
    }

    @Test
    fun `mine produces hash with correct leading zeros`() {
        val block = Block(0, 1000L, emptyList(), "0")
        val mined = block.mine(2)
        assertTrue(mined.hash.startsWith("00"))
    }

    @Test
    fun `mine changes nonce from default`() {
        val block = Block(0, 1000L, emptyList(), "0")
        val mined = block.mine(3)
        assertTrue(mined.nonce > 0)
        assertTrue(mined.hash.startsWith("000"))
    }

    @Test
    fun `mined block hash matches recomputed hash`() {
        val block = Block(0, 1000L, emptyList(), "0")
        val mined = block.mine(2)
        val recomputed = Block.computeHash(
            mined.nonce, mined.index, mined.timestamp, mined.transactions, mined.previousHash
        )
        assertEquals(mined.hash, recomputed)
    }
}
