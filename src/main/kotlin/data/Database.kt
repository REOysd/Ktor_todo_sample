package com.example.data

import com.example.data.tables.TodoTable
import com.example.data.tables.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun initDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;"
        driverClassName = "org.h2.Driver"
        maximumPoolSize = 3
        isAutoCommit = true
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.create(TodoTable)
        SchemaUtils.create(UserTable)
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }