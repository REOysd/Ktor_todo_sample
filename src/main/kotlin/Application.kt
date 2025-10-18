package com.example

import com.example.data.initDatabase
import com.example.plugin.security.EnvironmentConfig
import com.example.plugin.security.JWTConfig
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.h2.tools.Server
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    intercept(ApplicationCallPipeline.Setup) {
        println("Received request: ${call.request.uri}")
        proceed()
    }
    configureDependencyInjection()
    configureSerialization()
    configureDatabases()
    configureJwt()
    configureRouting()
}

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}

fun Application.configureDatabases() {
    initDatabase()

    val h2Server = Server.createWebServer("-webPort", "8082", "-webAllowOthers")
    h2Server.start()

    environment.monitor.subscribe(ApplicationStopped) {
        h2Server.stop()
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

fun Application.configureJwt() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = EnvironmentConfig.jwtRealm
            verifier(JWTConfig.verifier)

            validate { credential ->
                val tokenType = credential.payload.getClaim("type")?.asString()
                if (tokenType != "access") {
                    return@validate null
                }

                val userId = credential.payload.getClaim("userId")?.asInt()
                val userName = credential.payload.getClaim("userName")?.asString()

                if (userId != null && userName != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respondText(
                    text = """{"error":"トークンが無効または期限切れです"}""",
                    status = HttpStatusCode.Unauthorized,
                    contentType = ContentType.Application.Json
                )
            }
        }
    }
}