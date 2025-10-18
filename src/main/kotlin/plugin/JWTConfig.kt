package com.example.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JWTConfig {
    private val algorithm = Algorithm.HMAC256(EnvironmentConfig.jwtSecret)

    val verifier = JWT
        .require(algorithm)
        .withIssuer(EnvironmentConfig.jwtIssuer)
        .withAudience(EnvironmentConfig.jwtAudience)
        .build()

    fun generateAccessToken(userId: Int, userName: String): String {
        val now = Date()
        val expiresAt = Date(now.time + EnvironmentConfig.accessTokenValidity)

        return JWT.create()
            .withIssuer(EnvironmentConfig.jwtIssuer)
            .withAudience(EnvironmentConfig.jwtAudience)
            .withSubject(userId.toString())
            .withClaim("userId", userId)
            .withClaim("userName", userName)
            .withClaim("type", "access")
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .withJWTId(UUID.randomUUID().toString())
            .sign(algorithm)
    }

    fun generateRefreshToken(userId: Int): String {
        val now = Date()
        val expiresAt = Date(now.time + EnvironmentConfig.accessTokenValidity)

        return JWT.create()
            .withIssuer(EnvironmentConfig.jwtIssuer)
            .withAudience(EnvironmentConfig.jwtAudience)
            .withSubject(userId.toString())
            .withClaim("userId", userId)
            .withClaim("type", "refresh")
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .withJWTId(UUID.randomUUID().toString())
            .sign(algorithm)

    }
}