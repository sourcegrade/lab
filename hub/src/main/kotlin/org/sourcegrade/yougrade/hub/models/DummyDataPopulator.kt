package org.sourcegrade.yougrade.hub.models

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun main(args: Array<String>) {
    // read HOCON config file
    val config = com.typesafe.config.ConfigFactory.load()
    val dbConfig = config.getConfig("ktor.db")
    Database.connect(
        url = dbConfig.getString("url"),
        driver = "org.postgresql.Driver",
        user = dbConfig.getString("user"),
        password = dbConfig.getString("password"),
    )
    newSuspendedTransaction {
        addLogger(StdOutSqlLogger)

        // delete and re-create tables
        SchemaUtils.drop(Users)
        SchemaUtils.create(Users)

        val dummyUsers =
            listOf(
                User.new {
                    username = "John Doe"
                    email = "test@example.com"
//                    password = "test"
                },
                User.new {
                    username = "John Smith"
                    email = "john.smith@aol.com"
//                    password = "toast"
                },
                User.new {
                    username = "Bernd Scheuert"
                    email = "b.scheuert@gmail.com"
//                    password = "meinnameistbernd"
                },
            )
    }
}
