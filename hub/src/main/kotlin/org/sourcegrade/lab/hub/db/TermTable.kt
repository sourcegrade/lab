package org.sourcegrade.lab.hub.db

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object TermTable : UUIDTable("sgl_terms") {
    val name = varchar("name", 255).uniqueIndex()
    val start = timestamp("start")
    val end = timestamp("end")
}
