package umb.ink.ktor.bot

import net.mamoe.mirai.utils.BotConfiguration
import okhttp3.internal.toLongOrDefault

private enum class QQInfo(val value:String) {
    QQ("1149558764"),
    PASSWORD("Farewell1234."),
    PROTOCOL("PAD");
}
object QQConfig{
    val qq: Long = QQInfo.QQ.value.toLongOrDefault(0)
    val password: String = QQInfo.PASSWORD.value
    val protocol = when (QQInfo.PROTOCOL.value) {
        "PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
        "WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
        else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
    }
}