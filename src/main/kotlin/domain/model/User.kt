package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val email: String,
    val userName: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val userName: String,
    val password: String
)

@Serializable
data class UpdateUserRequest(
    val token: String,
    val user: User
)