@file:Suppress(
        "EXPERIMENTAL_API_USAGE",
        "DEPRECATION_ERROR",
        "OverridingDeprecatedMember",
        "INVISIBLE_REFERENCE",
        "INVISIBLE_MEMBER"
)

package io.farewell.github.qabot


import io.farewell.github.qabot.BotManager.BotsManager
import io.farewell.github.qabot.Listener.BaseListeners
import io.farewell.github.qabot.curd.logger
import kotlinx.coroutines.joinAll
import net.mamoe.mirai.event.registerEvents

suspend fun main() {
    // 添加监听job
    BaseListeners.listeners.forEach {
        BotsManager.registerEvents(it)
    }
    BotsManager.loginBot() //登录bot
    val logger = logger() // 打印日志
    joinAll(BotsManager.jobs) // 将BotsManager的监听事件加入协程
}