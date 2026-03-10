package blockchain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun WalletPanel(viewModel: AppViewModel) {
    Column {
        Button(onClick = { viewModel.createWallet() }) {
            Text("Create Wallet")
        }

        LazyColumn {
            itemsIndexed(viewModel.wallets) { index, wallet ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    backgroundColor = if (wallet == viewModel.selectedWallet.value)
                        MaterialTheme.colors.primaryVariant
                    else MaterialTheme.colors.surface,
                    elevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Wallet ${index + 1}", style =
                                    MaterialTheme.typography.subtitle1
                            )
                            Text(
                                formatAddress(wallet.publicKeyString),
                                style = MaterialTheme.typography.caption
                            )
                            Text(
                                "Balance: ${viewModel.blockchain.getBalance(wallet.publicKeyString)}", style =
                                    MaterialTheme.typography.body2
                            )
                        }
                        Button(onClick = { viewModel.selectedWallet.value = wallet }) {
                            Text("Select")
                        }
                    }
                }
            }
        }
    }
}
