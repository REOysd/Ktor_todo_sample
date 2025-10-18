package com.example.data.tables

import com.example.data.tables.TodoTable.clientDefault
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserTable: Table("Users") {
    val userid = integer("user_id").autoIncrement()
    val userName = varchar("user_name", 100).uniqueIndex()
    val email = varchar("email", 100).uniqueIndex()
    val passwordHash = varchar("password_hash", 100)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }

    override val primaryKey = PrimaryKey(userid)
}
