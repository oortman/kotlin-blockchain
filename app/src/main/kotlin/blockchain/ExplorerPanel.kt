package blockchain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import java.time.Instant.ofEpochMilli

@Composable
fun ExplorerPanel(viewModel: AppViewModel) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        itemsIndexed(viewModel.blockchain.chain) { index, block ->
            Card {
                Column {
                    Text("Block #${block.index}")
                    Text("Hash: ${formatAddress(block.hash)}")
                    Text("Previous: ${formatAddress(block.previousHash)}")
                    Text("Time: ${ofEpochMilli(block.timestamp)}")
                    Text("Nonce: ${block.nonce}")

                    var expanded by remember { mutableStateOf(false) }

                    TextButton(onClick = { expanded = !expanded }) {
                        Text("${if (expanded) "Hide" else "Show"} transactions (${block.transactions.size})")
                    }

                    if (expanded) {
                        block.transactions.forEach { tx ->
                            if (tx.sender == Transaction.COINBASE) {
                                Text("Mining Reward -> ${formatAddress(tx.recipient)}")
                            } else {
                                Text("${formatAddress(tx.sender)} -> ${formatAddress(tx.recipient)}: ${tx.amount}")
                            }
                        }
                    }
                }
            }
        }
    }
}