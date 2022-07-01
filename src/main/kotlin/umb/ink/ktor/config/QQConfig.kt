package umb.ink.ktor.config

import net.mamoe.mirai.utils.BotConfiguration
import okhttp3.internal.toLongOrDefault


object QQConfig{
    val qq: Long = QQInfo.QQ.value.toLongOrDefault(0)
    val password: String = QQInfo.PASSWORD.value
    val protocol = when (QQInfo.PROTOCOL.value) {
        "PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
        "WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
        else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
    }
}