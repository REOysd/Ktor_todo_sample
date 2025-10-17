package com.example.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Todo: Table("todos") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(User.id)
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id, name = "PK_Todo_ID")
}