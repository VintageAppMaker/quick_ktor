package com.psw

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.Principal
import java.util.*

data class Account(
    var name: String,
    var passwd : String ) : Principal

object JwtConfig {
    private const val secret = "7y3yfdshjkfhjkds7682dAAeecde" // use your own secret
    const val issuer = "com.psw"                              // use your own issuer
    private const val validityInMs = 36_000_00 * 24           // 1 day
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    /**
     * Produce a token for this combination of name and password
     */
    fun generateToken(user: Account): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("name", user.name)
        .withClaim("password", user.passwd)
        .withExpiresAt(getExpiration())  // optional
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

}