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
        val tables = SchemaUtils.listTables()
        SchemaUtils.drop(CourseMembers, Courses, Users)

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

        SchemaUtils.create(Courses)

        val dummyCourses =
            listOf(
                Course.new {
                    name = "Funktionale und objektorientierte Programmierkonzepte"
                    description =
                        "In diesem Kurs lernen Sie die Grundlagen der funktionalen und objektorientierten Programmierung kennen."
                    semesterType = SemesterType.WS
                    semesterStartYear = 2023
                },
                Course.new {
                    name = "Software Engineering"
                    description = "In diesem Kurs lernen Sie die Grundlagen des Software Engineerings kennen."
                    semesterType = SemesterType.SS
                    semesterStartYear = 2023
                },
                Course.new {
                    name = "Mathematik I für Informatiker"
                    description =
                        "In diesem Kurs lernen Sie die Grundlagen der Mathematik kennen. Dazu gehören unter anderem die Grundlagen der Analysis und der Linearen Algebra."
                    semesterType = SemesterType.WS
                    semesterStartYear = 2023
                },
            )

        SchemaUtils.create(CourseMembers)

        val dummyCourseMembers =
            listOf(
                CourseMember.new {
                    course = dummyCourses[0]
                    user = dummyUsers[0]
                },
                CourseMember.new {
                    course = dummyCourses[0]
                    user = dummyUsers[1]
                },
                CourseMember.new {
                    course = dummyCourses[1]
                    user = dummyUsers[1]
                },
                CourseMember.new {
                    course = dummyCourses[1]
                    user = dummyUsers[2]
                },
                CourseMember.new {
                    course = dummyCourses[2]
                    user = dummyUsers[0]
                },
                CourseMember.new {
                    course = dummyCourses[2]
                    user = dummyUsers[2]
                },
            )
    }
}
