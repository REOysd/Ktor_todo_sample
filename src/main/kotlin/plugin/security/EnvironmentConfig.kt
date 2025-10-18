package com.example.plugin.security

import io.github.cdimascio.dotenv.dotenv

object EnvironmentConfig {
    private val dotenv = dotenv {
        ignoreIfMissing = true
    }

    val jwtSecret: String = dotenv["JWT_SECRET"]
        ?: "your-super-secret-key-change-this-in-production-min-32-chars"

    val jwtIssuer: String = dotenv["JWT_ISSUER"]
        ?: "todo-app"

    val jwtAudience: String = dotenv["JWT_AUDIENCE"]
        ?: "todo-app-users"

    val jwtRealm: String = dotenv["JWT_REALM"]
        ?: "todo-app"

    val accessTokenValidity: Long = dotenv["ACCESS_TOKEN_VALIDITY_MS"]?.toLongOrNull()
        ?: 3600000L // 1 hour

    val refreshTokenValidity: Long = dotenv["REFRESH_TOKEN_VALIDITY_MS"]?.toLongOrNull()
        ?: 604800000L // 7 days

    val allowedHosts: List<String> = dotenv["ALLOWED_HOSTS"]?.split(",")
        ?: listOf("http://localhost:3000", "http://localhost:8080")
}