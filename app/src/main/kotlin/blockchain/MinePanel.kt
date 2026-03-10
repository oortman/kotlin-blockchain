package blockchain

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MinePanel(viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()
    val wallet = viewModel.selectedWallet.value

    if (wallet == null) {
        Text("Select a wallet first in the Wallets tab")
        return
    }

    Column {
        Text("Wallet key: ${formatAddress(wallet.publicKeyString)}")

        Button(
            onClick = {
                scope.launch {
                    viewModel.mineBlock(wallet)
                }
            },
            enabled = !viewModel.isMining.value
        ) {
            Text("Mine Block")
        }

        Row {
            if (viewModel.isMining.value) {
                Text("Mining... ")
                CircularProgressIndicator()
            } else {
                Text("Last mine result: ${viewModel.blockchain.chain.last().hash}")
            }
        }

        Column {
            Text("Current chain length: ${viewModel.blockchain.chain.size}")
            Text("Miner's balance: ${viewModel.blockchain.getBalance(wallet.publicKeyString)}")
        }
    }
}