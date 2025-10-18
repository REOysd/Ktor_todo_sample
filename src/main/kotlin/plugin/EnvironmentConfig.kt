package com.example.plugin

object EnvironmentConfig {
    val jwtSecret: String = System.getenv("JWT_SECRET")
        ?: throw IllegalStateException("JWT_SECRET環境変数が設定されていません")
    val jwtIssuer: String = System.getenv("JWT_ISSUER")
        ?: "todo-app"
    val jwtAudience: String = System.getenv("JWT_AUDIENCE")
        ?: "todo-app-audience"
    val jwtRealm: String = System.getenv("JWT_REALM")
        ?: "todo-app-realm"

    val accessTokenValidity: Long = System.getenv("ACCESS_TOKEN_VALIDITY_MS")?.toLongOrNull()
        ?: 36_000_00L
    val refreshTokenValidity: Long = System.getenv("REFRESH_TOKEN_VALIDITY_MS")?.toLongOrNull()
        ?: 86_400_000L

    val allowedHosts: List<String> = System.getenv("ALLOWED_HOSTS")?.split(",")
        ?: listOf("http://localhost:3000")

}