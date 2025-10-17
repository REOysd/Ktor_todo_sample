package com.example.data.tables

import org.jetbrains.exposed.sql.Table

object UserTable: Table("Users") {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 200).uniqueIndex()
    val userName = varchar("user_name", 100)
    val passwordHash = varchar("password_hash", 255)
    override val primaryKey = PrimaryKey(id, name = "PK_User_ID")
}