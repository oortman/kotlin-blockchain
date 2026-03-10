package blockchain

fun formatAddress(address: String): String {
    return "${address.take(4)} ${address.substring(4, 8)}...${
        address.substring(
            address.length - 8,
            address.length - 4
        )
    } ${address.takeLast(4)}"
}
