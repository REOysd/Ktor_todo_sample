package com.example.domain.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Todo(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String?,
) {
    companion object {
        fun fromRow(row: ResultRow): Todo {
            return Todo(
                id = row[com.example.data.tables.TodoTable.id],
                userId = row[com.example.data.tables.TodoTable.userId],
                title = row[com.example.data.tables.TodoTable.title],
                description = row[com.example.data.tables.TodoTable.description]
            )
        }
    }
}

@Serializable
data class CreateTodoRequest(
    val title: String,
    val description: String? = null
)