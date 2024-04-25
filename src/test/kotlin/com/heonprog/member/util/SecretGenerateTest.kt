package com.heonprog.member.util

import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.FunSpec
import java.util.*

class SecretGenerateTest : FunSpec({
    test("secretKey generate") {
        val secretKeyPlain = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey"
        val keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.toByteArray())
        val secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.toByteArray())

        println(secretKey)
    }
})