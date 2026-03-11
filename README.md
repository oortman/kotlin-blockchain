# Kotlin Blockchain

A blockchain implementation in Kotlin with a Compose Desktop UI, built to explore both blockchain concepts and Kotlin language features.

## Features

- Blocks with SHA-256 hashing
- Proof of Work mining with configurable difficulty
- Chain integrity validation
- Signed transactions using ECDSA public-key cryptography
- Wallet key pair generation and balance tracking
- Transaction memory pool (mempool) with validation
- Desktop UI for interacting with the blockchain

## Desktop UI

The app includes a Compose Desktop interface with four panels:

- **Wallets** — Create wallets, view balances, and select the active wallet
- **Explorer** — Browse all blocks in the chain, expand to view transactions, and verify chain integrity
- **Send** — Submit signed transactions to the mempool
- **Mine** — Mine new blocks with Proof of Work (runs asynchronously via coroutines)

## Project structure

```
app/src/main/kotlin/blockchain/
├── Block.kt            # Block data class, SHA-256 hashing, mining
├── Blockchain.kt       # Chain management, genesis block, validation
├── Mempool.kt          # Transaction pool with signature and balance checks
├── Transaction.kt      # Transaction record with optional signature
├── Wallet.kt           # EC key pair, sign and verify
├── AppViewModel.kt     # Reactive state management for the UI
├── Main.kt             # Compose Desktop window and navigation
├── WalletPanel.kt      # Wallet creation and selection UI
├── MinePanel.kt        # Mining interface with async progress
├── SendPanel.kt        # Transaction submission form
├── ExplorerPanel.kt    # Block and transaction explorer
└── Utils.kt            # Address formatting utilities
```

## Running

```bash
./gradlew run
```

## Testing

```bash
./gradlew test
```

## Requirements

- Java 21
- Gradle 9.3.1 (included via wrapper)