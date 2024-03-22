package org.sourcegrade.lab.hub

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, module = Application::module)
        .start(wait = true)
}
