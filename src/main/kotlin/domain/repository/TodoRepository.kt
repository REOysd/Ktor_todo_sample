package com.example.domain.repository

import com.example.domain.model.CreateTodoRequest
import com.example.domain.model.Todo

interface TodoRepository {
    suspend fun findAllByUserId(userId: Int): List<Todo>
    suspend fun createTodo(userId: Int, request: CreateTodoRequest): Todo
    suspend fun delete(userId: Int, id: Int): Boolean
}