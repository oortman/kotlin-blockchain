package blockchain

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication, title = "Kotlin Blockchain",
        state = rememberWindowState(width = 1080.dp, height = 720.dp)
    ) {
        val viewModel = remember { AppViewModel() }
        var currentPanel by remember { mutableStateOf("wallets") }

        MaterialTheme {
            Row(modifier = Modifier.fillMaxSize()) {
                // Left sidebar
                Column(
                    modifier = Modifier.width(200.dp).fillMaxHeight().padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Navigation
                    Button(
                        onClick = { currentPanel = "wallets" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Wallets")
                    }

                    Button(
                        onClick = { currentPanel = "explorer" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Explorer")
                    }

                    Button(
                        onClick = { currentPanel = "send" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Send")
                    }

                    Button(
                        onClick = { currentPanel = "mine" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mine")
                    }
                }

                // Right content area
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    when (currentPanel) {
                        "wallets" -> WalletPanel(viewModel)
                        "explorer" -> Text("Explorer coming soon")
                        "send" -> Text("Send coming soon")
                        "mine" -> MinePanel(viewModel)
                    }

                }
            }
        }
    }
}
