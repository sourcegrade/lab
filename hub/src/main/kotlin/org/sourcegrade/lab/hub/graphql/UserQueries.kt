package org.sourcegrade.lab.hub.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.sourcegrade.lab.hub.domain.User

class UserQueries : Query {
    suspend fun user(
        environment: DataFetchingEnvironment,
        id: String? = null
    ): UserQuery = UserQuery()
}

@GraphQLDescription("Query user collection")
class UserQuery {
    suspend fun fetch(environment: DataFetchingEnvironment): User {

    }
}
