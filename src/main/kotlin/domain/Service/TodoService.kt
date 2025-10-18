package com.example.domain.Service

import com.example.domain.model.CreateTodoRequest
import com.example.domain.model.Todo
import com.example.domain.repository.TodoRepository

class TodoService(private val todoRepository: TodoRepository) {
    suspend fun getAllTodo(): List<Todo> {
        return todoRepository.findAll()
    }

    suspend fun createTodo(request: CreateTodoRequest): Todo {
        if (request.title.isBlank()) {
            throw IllegalArgumentException("title cannot be empty")
        }
        return todoRepository.createTodo(request)
    }

    suspend fun deleteTodo(id: Int): Boolean {
        return todoRepository.delete(id)
    }
}