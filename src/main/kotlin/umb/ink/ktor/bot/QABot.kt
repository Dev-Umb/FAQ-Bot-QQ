package umb.ink.ktor.bot

import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import umb.ink.ktor.bot.message.BaseListener
import java.io.File

class QABot: BotConfiguration() {
    companion object{
        private val bot = BotFactory.newBot(qq = QQConfig.qq,
            password = QQConfig.password,
            configuration =  BotConfiguration {
                protocol = QQConfig.protocol
                val deviceInfoFolder = File("devices")
                if (!deviceInfoFolder.exists()) {
                    deviceInfoFolder.mkdir()
                }
                fileBasedDeviceInfo(File(
                    deviceInfoFolder,
                    "${QQConfig.qq}-device.json").absolutePath
                )
            })
        fun login(): Bot {
            return runBlocking {
                    bot.login()
                    BaseListener.listeners.forEach {
                        bot.eventChannel.registerListenerHost(it)
                    }
                    return@runBlocking bot
            }
        }
        suspend fun reLogin(): Bot{
            bot.login()
            return bot
        }
    }
}