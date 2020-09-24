@file:Suppress(
        "EXPERIMENTAL_API_USAGE",
        "DEPRECATION_ERROR",
        "OverridingDeprecatedMember",
        "INVISIBLE_REFERENCE",
        "INVISIBLE_MEMBER"
)


import BotManager.BotsManager
import Listener.BaseListeners
import curd.logger
import kotlinx.coroutines.joinAll
import net.mamoe.mirai.event.registerEvents
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content

suspend fun main() {
    // 添加监听job
    BaseListeners.listeners.forEach {
        BotsManager.registerEvents(it)
    }
    BotsManager.loginBot() //登录bot
    val logger = logger() // 打印日志
    joinAll(BotsManager.jobs) // 将BotsManager的监听事件加入协程
}