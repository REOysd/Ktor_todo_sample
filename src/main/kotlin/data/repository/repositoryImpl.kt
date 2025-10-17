package com.example.data.repository

import com.example.data.dbQuery
import com.example.data.tables.TodoTable
import com.example.domain.model.CreateTodoRequest
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RepositoryImpl: TodoRepository {
    override suspend fun findAll(): List<Todo> = dbQuery {
        TodoTable.selectAll().map { Todo.fromRow(it) }
    }

    override suspend fun createTodo(userId: Int, request: CreateTodoRequest): Todo = dbQuery {
        val insertStatement = TodoTable.insert {
            it[TodoTable.userId] = userId
            it[title] = request.title
            it[description] = request.description
        }
        insertStatement.resultedValues?.first()?.let { Todo.fromRow(it) }
            ?: throw Exception("Failed to create todo")
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        TodoTable.deleteWhere { TodoTable.id eq id } > 0
    }
}