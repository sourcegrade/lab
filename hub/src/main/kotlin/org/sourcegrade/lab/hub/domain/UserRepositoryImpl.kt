package org.sourcegrade.lab.hub.domain

import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.sourcegrade.lab.hub.db.UserTable
import java.util.UUID

class UserRepositoryImpl(
    private val logger: Logger
) : UserRepository {

    override suspend fun create(item: User.CreateDto): User = newSuspendedTransaction {
        val result = UserTable.insert {
            it[username] = item.username
            it[email] = item.email
        }.resultedValues

        val user = checkNotNull(result) { "Failed to create User ${item.username}" }
            .single().toUser()

        logger.info("Created new User ${user.id} with data $item")

        user
    }

    override suspend fun put(item: User.CreateDto): MutableRepository.PutResult<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: UUID): User? {
        TODO("Not yet implemented")
    }

    override suspend fun exists(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun countAll(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: UUID): Boolean {
        TODO("Not yet implemented")
    }
}
