package blockchain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant.ofEpochMilli

@Composable
fun ExplorerPanel(viewModel: AppViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Chain valid: ${if (viewModel.blockchain.isValid()) "Yes" else "No"}",
            style = MaterialTheme.typography.caption,
            color = if (viewModel.blockchain.isValid()) MaterialTheme.colors.secondary
                    else MaterialTheme.colors.error
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(viewModel.blockchain.chain) { index, block ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Block #${block.index}", style = MaterialTheme.typography.h6)

                        Text("Hash: ${formatAddress(block.hash)}", style = MaterialTheme.typography.caption)
                        Text("Previous: ${formatAddress(block.previousHash)}", style = MaterialTheme.typography.caption)

                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Text("Time: ${ofEpochMilli(block.timestamp)}", style = MaterialTheme.typography.body2)
                            Text("Nonce: ${block.nonce}", style = MaterialTheme.typography.body2)
                        }

                        var expanded by remember { mutableStateOf(false) }

                        TextButton(onClick = { expanded = !expanded }) {
                            Text("${if (expanded) "▾ Hide" else "▸ Show"} transactions (${block.transactions.size})")
                        }

                        if (expanded) {
                            Column(
                                modifier = Modifier.padding(start = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                block.transactions.forEach { tx ->
                                    if (tx.sender == Transaction.COINBASE) {
                                        Text(
                                            "Mining Reward → ${formatAddress(tx.recipient)} (+50.0)",
                                            color = MaterialTheme.colors.secondary,
                                            style = MaterialTheme.typography.body2
                                        )
                                    } else {
                                        Text(
                                            "${formatAddress(tx.sender)} → ${formatAddress(tx.recipient)}: ${tx.amount}",
                                            style = MaterialTheme.typography.body2
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}