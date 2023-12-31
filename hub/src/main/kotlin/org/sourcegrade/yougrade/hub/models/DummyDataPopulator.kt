package org.sourcegrade.yougrade.hub.models

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {
    Database.connect(
        "jdbc:postgresql://localhost:5432/yougrade",
        driver = "org.postgresql.Driver",
        user = "admin",
        password = "admin",
    )
    transaction {
        addLogger(StdOutSqlLogger)

        // delete and re-create tables
        SchemaUtils.drop(Users)
        SchemaUtils.create(Users)

        val dummyUsers =
            listOf(
                Users.createUser(
                    username = "John Doe",
                    email = "test@example.com",
                    password = "test",
                ),
                Users.createUser(
                    username = "John Smith",
                    email = "john.smith@aol.com",
                    password = "toast",
                ),
                Users.createUser(
                    username = "Bernd Scheuert",
                    email = "b.scheuert@gmail.com",
                    password = "meinnameistbernd",
                ),
            )
    }
}
