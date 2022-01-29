package com.psw

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.Principal
import java.util.*


// session을 위한 data 클래스
// import io.ktor.auth.Principal 임을 반드시 채크
data class Account(
    var name: String,
    var passwd : String ) : Principal

// jwt(json web token)은
// 해더(header).내용(payload).서명(signature) 구조로 되어있다.

object JwtHelper {
    private const val secret = "7y3yfdshjkfhjkds7682dAAeecde" // 검증을 위한 알고리즘 secret 값
    const val issuer = "com.psw"                              // issuer(토큰발급자)
    private const val validTime = 36_000_00 * 24              // 토큰유효 기간
    private val algorithm = Algorithm.HMAC512(secret)         // 알고리즘

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun buildToken(user: Account): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("name", user.name)
        .withClaim("password", user.passwd)
        .withExpiresAt(getExpTime())
        .sign(algorithm)

    private fun getExpTime() = Date(System.currentTimeMillis() + validTime)

}