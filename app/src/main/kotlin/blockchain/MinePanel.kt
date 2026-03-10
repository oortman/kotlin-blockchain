package blockchain

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MinePanel(viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()
    val wallet = viewModel.selectedWallet.value

    if (wallet == null) {
        Text("Select a wallet first in the Wallets tab")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            if (viewModel.isMining.value) {
                Text("Mining... ")
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Last mine result: ${viewModel.blockchain.chain.last().hash}")
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Current chain length: ${viewModel.blockchain.chain.size}")
            Text("Miner's balance: ${viewModel.blockchain.getBalance(wallet.publicKeyString)}")
        }
    }
}