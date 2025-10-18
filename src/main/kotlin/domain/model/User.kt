package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int,
    val userName: String,
    val email: String
)

@Serializable
data class UserRegistration(
    val userName: String,
    val email: String,
    val password: String
)

@Serializable
data class UserLogin(
    val userName: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: User
)