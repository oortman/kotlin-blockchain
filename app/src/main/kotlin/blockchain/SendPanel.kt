package blockchain

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

@Composable
fun SendPanel(viewModel: AppViewModel) {
    val wallet = viewModel.selectedWallet.value
    var recipientAddress by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val parsedAmount = amount.toDoubleOrNull()
    var successMessage by remember { mutableStateOf<String?>(null) }

    if (wallet == null) {
        Text("Select a wallet first in the Wallets tab")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Wallet key: ${formatAddress(wallet.publicKeyString)}")
        Text("Balance: ${viewModel.blockchain.getBalance(wallet.publicKeyString)}")

        Row {
            Text("Recipient address: ")
            TextField(
                value = recipientAddress,
                onValueChange = { recipientAddress = it },
                label = { Text("Recipient Address") }
            )
            viewModel.wallets.filter { it != wallet }.forEachIndexed { index, otherWallet ->
                TextButton(onClick = { recipientAddress = otherWallet.publicKeyString }) {
                    Text("Wallet ${viewModel.wallets.indexOf(otherWallet) + 1}: ${formatAddress(otherWallet.publicKeyString)}")
                }
            }

        }

        Row {
            Text("Amount to send: ")
            TextField(
                value = amount,
                onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) amount = it }
            )
        }

        Button(
            onClick = {
                successMessage = null
                if (viewModel.submitTransaction(wallet, recipientAddress, parsedAmount!!)) {
                    successMessage = "Sent $parsedAmount to${formatAddress(recipientAddress)}"
                    amount = ""
                    recipientAddress = ""
                }
            },
            enabled = (parsedAmount != null && parsedAmount > 0.0 && recipientAddress.isNotBlank())
        ) {
            Text("Send")
        }

        viewModel.errorMessage.value?.let {
            Text(it, color = MaterialTheme.colors.error)
        }
        successMessage?.let {
            Text(it, color = MaterialTheme.colors.primary)
        }
    }
}
