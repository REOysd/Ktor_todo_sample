package com.example

import com.example.data.repository.RepositoryImpl
import com.example.domain.Service.TodoService
import com.example.domain.Service.UserService
import com.example.domain.repository.TodoRepository
import org.koin.dsl.module

val appModule = module {
    single<TodoRepository> { RepositoryImpl()}

    single { TodoService(get()) }
    single { UserService() }
}