package com.example.domain.model

import com.example.data.tables.TodoTable
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class Todo(
    val id: Int,
    val title: String,
    val description: String?,
) {
    companion object {
        fun fromRow(row: ResultRow): Todo {
            return Todo(
                id = row[TodoTable.id],
                title = row[TodoTable.title],
                description = row[TodoTable.description]
            )
        }
    }
}

@Serializable
data class CreateTodoRequest(
    val title: String,
    val description: String? = null
)