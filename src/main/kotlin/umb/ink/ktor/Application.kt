package umb.ink.ktor

import umb.ink.ktor.bot.QABot

suspend fun main() {
    QABot.login()
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
//        configureSecurity()
//        configureHTTP()
//        QABot.login()
//        configureSerialization()
//    }.start(wait = true)
}
