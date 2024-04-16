package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.sourcegrade.lab.hub.db.UserTable
import java.util.UUID

//data class User(
//    override val id: UUID,
//    val username: String,
//    val email: String,
//) : DomainEntity {
//    data class CreateDto(
//        val username: String,
//        val email: String,
//    ) : Creates<User>
//}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    val username by UserTable.username
    val email by UserTable.email

    companion object : EntityClass<UUID, User>(UserTable)
}
