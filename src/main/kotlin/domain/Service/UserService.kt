package com.example.domain.Service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.data.tables.UserTable
import com.example.domain.model.User
import com.example.domain.model.UserLogin
import com.example.domain.model.UserRegistration
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserService {
    fun createUser(registration: UserRegistration): User? = transaction {
        try {
            val passwordHash = BCrypt.withDefaults().hashToString(12, registration.password.toCharArray())

            val userId = UserTable.insert {
                it[userName] = registration.userName
                it[email] = registration.email
                it[UserTable.passwordHash] = passwordHash
            } get UserTable.userid

            User(
                userId = userId,
                userName = registration.userName,
                email = registration.email
            )
        } catch (e: Exception) {
            null
        }
    }

    fun authenticate(login: UserLogin): User? = transaction {
        UserTable.select { UserTable.userName eq login.userName }
            .mapNotNull { row ->
                val passwordHash = row[UserTable.passwordHash]

                val result = BCrypt.verifyer()
                    .verify(login.password.toCharArray(), passwordHash)
                if (result.verified) {
                    User(
                        userId = row[UserTable.userid],
                        userName = row[UserTable.userName],
                        email = row[UserTable.email]
                    )
                } else {
                    null
                }
            }
            .singleOrNull()
    }

    fun getUserById(userId: Int): User? = transaction {
        UserTable.select { UserTable.userid eq userId }
            .mapNotNull { row ->
                User(
                    userId = row[UserTable.userid],
                    userName = row[UserTable.userName],
                    email = row[UserTable.email]
                )
            }
            .singleOrNull()
    }
}