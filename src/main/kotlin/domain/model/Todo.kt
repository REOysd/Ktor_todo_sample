package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String?,
)

@Serializable
data class CreateTodoRequest(
    val title: String,
    val description: String? = null
)