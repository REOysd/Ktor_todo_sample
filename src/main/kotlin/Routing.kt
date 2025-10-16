package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("みんなや！")
        }
        get("/greet") {
            val name = call.request.queryParameters["name"] ?: "Guest"
            call.respondText("Hello, $name!")
        }
        get("/divide") {
            try {
                val a = call.request.queryParameters["a"]?.toInt() ?: throw IllegalArgumentException("parameter 'a' is missing")
                val b = call.request.queryParameters["b"]?.toInt() ?: throw IllegalArgumentException("parameter 'b' is missing")
                val result = a / b
                call.respondText("result: $result")
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}", status = io.ktor.http.HttpStatusCode.InternalServerError)
            }
        }
    }
}
