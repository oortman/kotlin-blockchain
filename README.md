# Kotlin Blockchain

A simple blockchain implementation in Kotlin, built to explore both blockchain concepts and Kotlin language features.

## Features

- Blocks with SHA-256 hashing
- Proof of Work mining with configurable difficulty
- Chain integrity validation
- Signed transactions using ECDSA public-key cryptography
- Wallet key pair generation

## Project structure

```
app/src/main/kotlin/blockchain/
├── Block.kt          # Block data class, SHA-256 hashing, mining
├── Blockchain.kt     # Singleton chain, genesis block, validation
├── Transaction.kt    # Transaction record with optional signature
├── Wallet.kt         # EC key pair, sign and verify
└── Main.kt           # Entry point and demo
```

## Running

```bash
./gradlew run
```

Requires Java 21.
