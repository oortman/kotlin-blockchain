package blockchain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

enum class Panel { Wallets, Explorer, Send, Mine }

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication, title = "Kotlin Blockchain",
        state = rememberWindowState(width = 1080.dp, height = 720.dp)
    ) {
        val viewModel = remember { AppViewModel() }
        var currentPanel by remember { mutableStateOf(Panel.Wallets) }

        MaterialTheme(colors = darkColors(
            primary = Color(0xFF81D4FA),
            primaryVariant = Color(0xFF0277BD),
            secondary = Color(0xFFA5D6A7),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            error = Color(0xFFEF5350),
            onPrimary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Row {
                    // Left sidebar
                    Column(
                        modifier = Modifier
                            .width(180.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colors.surface)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "Blockchain",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Panel.entries.forEach { panel ->
                            if (currentPanel == panel) {
                                Button(
                                    onClick = {},
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.primary
                                    )
                                ) {
                                    Text(panel.name, color = MaterialTheme.colors.onPrimary)
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { currentPanel = panel },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(panel.name)
                                }
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        Text(
                            "Pending: ${viewModel.mempool.pending.size}",
                            style = MaterialTheme.typography.caption
                        )
                        Text(
                            "Blocks: ${viewModel.blockchain.chain.size}",
                            style = MaterialTheme.typography.caption
                        )
                    }

                    Divider(modifier = Modifier.fillMaxHeight().width(1.dp))

                    // Right content area
                    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                        when (currentPanel) {
                            Panel.Wallets -> WalletPanel(viewModel)
                            Panel.Explorer -> ExplorerPanel(viewModel)
                            Panel.Send -> SendPanel(viewModel)
                            Panel.Mine -> MinePanel(viewModel)
                        }
                    }
                }
            }
        }
    }
}
