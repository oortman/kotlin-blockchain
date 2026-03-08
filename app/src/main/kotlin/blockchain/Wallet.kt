package blockchain

import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

class Wallet {
    val keyPair = KeyPairGenerator.getInstance("EC").apply { initialize(256) }.generateKeyPair()
    val privateKey = keyPair.private
    val publicKey = keyPair.public
    val publicKeyString: String = Base64.getEncoder().encodeToString(publicKey.encoded)

    companion object {
        fun verify(transaction: Transaction): Boolean {
            val sig = Signature.getInstance("SHA256withECDSA")
            val keyBytes = Base64.getDecoder().decode(transaction.sender)
            val publicKey =
                    KeyFactory.getInstance("EC").generatePublic(X509EncodedKeySpec(keyBytes))
            val signature = transaction.signature ?: return false

            sig.initVerify(publicKey)
            sig.update(
                    "${transaction.sender}${transaction.recipient}${transaction.amount}".toByteArray()
            )

            return sig.verify(signature)
        }
    }

    fun sign(transaction: Transaction): Transaction {
        val sig = Signature.getInstance("SHA256withECDSA")

        sig.initSign(privateKey)
        sig.update(
                "${transaction.sender}${transaction.recipient}${transaction.amount}".toByteArray()
        )

        return transaction.copy(signature = sig.sign())
    }
}
