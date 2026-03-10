package blockchain

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Panel { Wallets, Explorer, Send, Mine }

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication, title = "Kotlin Blockchain",
        state = rememberWindowState(width = 1080.dp, height = 720.dp)
    ) {
        val viewModel = remember { AppViewModel() }
        var currentPanel by remember { mutableStateOf(Panel.Wallets) }

        MaterialTheme {
            Row(modifier = Modifier.fillMaxSize()) {
                // Left sidebar
                Column(
                    modifier = Modifier.width(200.dp).fillMaxHeight().padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Panel.entries.forEach { panel ->
                        Button(
                            onClick = { currentPanel = panel },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(panel.name)
                        }
                    }
                }

                // Right content area
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    when (currentPanel) {
                        Panel.Wallets -> WalletPanel(viewModel)
                        Panel.Explorer -> Text("Explorer coming soon")
                        Panel.Send -> Text("Send coming soon")
                        Panel.Mine -> MinePanel(viewModel)
                    }

                }
            }
        }
    }
}
