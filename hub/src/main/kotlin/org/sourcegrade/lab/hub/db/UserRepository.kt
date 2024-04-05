package org.sourcegrade.lab.hub.db

import org.sourcegrade.lab.hub.models.User

class UserRepository {
    suspend fun getByUsername(username: String): User? {
        TODO()
    }
    suspend fun getByEmail(email: String): User? {
        TODO()
    }
}
