package com.example.domain.repository

import com.example.domain.model.CreateTodoRequest
import com.example.domain.model.Todo

interface TodoRepository {
    suspend fun findAll(): List<Todo>
    suspend fun createTodo(userId: Int, request: CreateTodoRequest): Todo
    suspend fun delete(id: Int): Boolean
}