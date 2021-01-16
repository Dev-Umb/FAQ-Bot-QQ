@file:Suppress(
        "EXPERIMENTAL_API_USAGE",
        "DEPRECATION_ERROR",
        "OverridingDeprecatedMember",
        "INVISIBLE_REFERENCE",
        "INVISIBLE_MEMBER"
)

package io.farewell12345.github.faqbot

import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.BotManager.PicManager
import io.farewell12345.github.faqbot.Listener.BaseListeners
import io.farewell12345.github.faqbot.DTO.model.logger
import io.farewell12345.github.faqbot.Task.TimerSessionManager
import kotlinx.coroutines.joinAll
import net.mamoe.mirai.event.registerTo
import java.util.*

suspend fun main() {
    // 添加监听job
    val bot = BotsManager.loginBot()  //登录bot
    BaseListeners.listeners.forEach {
        bot.eventChannel.registerListenerHost(it)
    }
    PicManager
    CommandGroupList.welcomeGroupList= LinkedList()
    CommandGroupList.managerGroupList = LinkedList()
    CommandGroupList.GameMorningGroupList = LinkedList()
    CommandGroupList.DisRepetitionGroupList = LinkedList()
    CommandGroupList.AnimationGroupList  = LinkedList()
//    Timer().schedule(BotsManager.task,Date(),300)
    val logger = logger() // 打印日志
    joinAll(BotsManager.jobs) // 将BotsManager的监听事件加入协程
}