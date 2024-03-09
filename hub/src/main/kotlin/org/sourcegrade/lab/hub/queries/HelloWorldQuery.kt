package org.sourcegrade.lab.hub.queries

import com.expediagroup.graphql.server.operations.Query

class HelloWorldQuery : Query {
    fun helloWorld(name: String? = null) = "Hello, ${name ?: "World"}!"
}
