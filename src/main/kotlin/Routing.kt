package com.example

import com.example.domain.Service.TodoService
import com.example.domain.Service.UserService
import com.example.domain.model.AuthResponse
import com.example.domain.model.CreateTodoRequest
import com.example.domain.model.UserLogin
import com.example.domain.model.UserRegistration
import com.example.plugin.security.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val todoService by inject<TodoService>()
    val userService by inject<UserService>()

    routing {
        todoRoutes(todoService)
        authRoutes(userService)
        get("/health") {
            call.respond(mapOf("status" to "ok"))
        }
    }
}

fun Route.todoRoutes(todoService: TodoService) {
    authenticate("auth-jwt") {
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
}

fun Route.authRoutes(userService: UserService) {
    route("/api/auth") {
        post("/register") {
            try {
                val registration = call.receive<UserRegistration>()

                if (registration.userName.length < 7) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "ユーザー名は７文字以上必要です")
                    )
                    return@post
                }

                val user = userService.createUser(registration)

                if (user != null) {
                    val token = JWTConfig.generateAccessToken(user.userId, user.userName)
                    call.respond(HttpStatusCode.Created, AuthResponse(token, user))
                } else {
                    call.respond(
                        HttpStatusCode.Conflict,
                        mapOf("error" to "ユーザー名またはメールアドレスがすでに使用されています")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "不正なリクエストです")
                )
            }
        }

        post("/login") {
            try {
                val login = call.receive<UserLogin>()
                val user = userService.authenticate(login)

                if (user != null) {
                    val token = JWTConfig.generateAccessToken(user.userId, user.userName)
                    call.respond(AuthResponse(token, user))
                } else {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        mapOf("error" to "ユーザー名またはパスワードが正しくありません")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "不正なリクエストです")
                )
            }
        }
    }
}