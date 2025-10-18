package com.example

import com.example.domain.Service.TodoService
import com.example.domain.model.CreateTodoRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        todoRoutes()
    }
}

fun Route.todoRoutes() {
    val todoService by inject<TodoService>()

    route("/todos") {
        get {
            try {
                val todos = todoService.getAllTodo()
                call.respond(HttpStatusCode.OK, todos)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error"))
                )
            }
        }

        post {
            try {
                val request = call.receive<CreateTodoRequest>()
                val todo = todoService.createTodo(request)
                call.respond(HttpStatusCode.Created, todo)
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to e.message)
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Failed to create todo: ${e.message}")
                )
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid ID")
                )

            try {
                val deleted = todoService.deleteTodo(id)

                if (deleted) {
                    call.respond(HttpStatusCode.OK,
                        mapOf("message" to "Todo deleted successfully")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("error" to "Todo not found")
                    )
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to e.message)
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error"))
                )
            }
        }
    }
}
