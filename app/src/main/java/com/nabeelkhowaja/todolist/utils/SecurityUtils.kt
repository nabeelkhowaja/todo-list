package com.nabeelkhowaja.todolist.utils

import android.util.Base64
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {

    /**
     * The Constant ALGORITHM.
     */
    private val ALGORITHM = "AES"

    /**
     * The Constant ITERATIONS.
     */
    private val ITERATIONS = 2

    /**
     * The Constant keyValue.
     */
    private val keyValue = byteArrayOf(
        'L'.toByte(),
        'o'.toByte(),
        'v'.toByte(),
        'e'.toByte(),
        'Y'.toByte(),
        'o'.toByte(),
        'u'.toByte(),
        'r'.toByte(),
        'P'.toByte(),
        'a'.toByte(),
        'k'.toByte(),
        'i'.toByte(),
        's'.toByte(),
        't'.toByte(),
        'a'.toByte(),
        'n'.toByte()
    )

    @Throws(Exception::class)
    fun encrypt(value: String, salt: String): String? {
        val key = generateKey()
        val c = Cipher.getInstance(ALGORITHM)
        c.init(Cipher.ENCRYPT_MODE, key)
        var valueToEnc: String
        var eValue = value
        for (i in 0 until ITERATIONS) {
            valueToEnc = salt + eValue
            val encValue = c.doFinal(valueToEnc.toByteArray())
            eValue = Base64.encodeToString(encValue, Base64.DEFAULT)
        }
        return eValue
    }

    @Throws(Exception::class)
    fun decrypt(value: String?, salt: String): String? {
        val key = generateKey()
        val c = Cipher.getInstance(ALGORITHM)
        c.init(Cipher.DECRYPT_MODE, key)
        var dValue: String? = null
        var valueToDecrypt = value
        for (i in 0 until ITERATIONS) {
            val decordedValue =
                Base64.decode(valueToDecrypt, Base64.DEFAULT)
            val decValue = c.doFinal(decordedValue)
            dValue = String(decValue).substring(salt.length)
            valueToDecrypt = dValue
        }
        return dValue
    }

    @Throws(Exception::class)
    private fun generateKey(): Key {
        return SecretKeySpec(
            keyValue,
            ALGORITHM
        )
    }
}