package com.example.domain.Service

import com.example.domain.model.CreateTodoRequest
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository

class TodoService(private val todoRepository: TodoRepository) {
    suspend fun getAllTodo(userId: Int): List<Todo> {
        return todoRepository.findAllByUserId(userId)
    }

    suspend fun createTodo(userId: Int, request: CreateTodoRequest): Todo {
        if (request.title.isBlank()) {
            throw IllegalArgumentException("title cannot be empty")
        }
        return todoRepository.createTodo(userId, request)
    }

    suspend fun deleteTodo(userId: Int, id: Int): Boolean {
        return todoRepository.delete(userId, id)
    }
}