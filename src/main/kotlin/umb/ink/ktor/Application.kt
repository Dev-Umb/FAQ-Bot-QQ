package umb.ink.ktor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import umb.ink.ktor.bot.QABot
import umb.ink.ktor.plugins.*
import umb.ink.ktor.config.configureHTTP

suspend fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSecurity()
        configureHTTP()
        QABot.login()
        configureSerialization()
    }.start(wait = true)
}
