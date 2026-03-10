package blockchain

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*

class AppViewModel {
    val blockchain = Blockchain()
    val mempool = Mempool(blockchain)
    val wallets = mutableStateListOf<Wallet>()
    val errorMessage = mutableStateOf<String?>(null)
    val selectedWallet = mutableStateOf<Wallet?>(null)
    val isMining = mutableStateOf(false)

    fun createWallet() {
        val newWallet = Wallet()
        wallets.add(newWallet)

        if (selectedWallet.value == null) {
            selectedWallet.value = newWallet
        }
    }

    fun submitTransaction(sender: Wallet, recipient: String, amount: Double) {
        val transaction = sender.sign(Transaction(sender.publicKeyString, recipient, amount))

        try {
            mempool.submit(transaction)
        } catch (e: Exception) {
            errorMessage.value = e.message
        }
    }

    suspend fun mineBlock(miner: Wallet) {
        isMining.value = true

        try {
            withContext(Dispatchers.Default) {
                blockchain.addBlock(mempool.pull(10), miner.publicKeyString)
            }
        } finally {
            isMining.value = false
        }
    }
}