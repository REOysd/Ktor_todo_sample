package com.example

import com.example.domain.Service.TodoService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        get("/") {
            val response = mapOf("message" to "Hello, API!")
            call.respond(response)
        }
        get("/greet") {
            val name = call.request.queryParameters["name"] ?: "Guest"
            call.respondText("Hello, $name!")
        }
        get("/divide") {
            try {
                val a = call.request.queryParameters["a"]?.toInt()
                    ?: throw IllegalArgumentException("parameter 'a' is missing")
                val b = call.request.queryParameters["b"]?.toInt()
                    ?: throw IllegalArgumentException("parameter 'b' is missing")
                val result = a / b
                call.respondText("result: $result")
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = io.ktor.http.HttpStatusCode.InternalServerError)
            }
        }
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
