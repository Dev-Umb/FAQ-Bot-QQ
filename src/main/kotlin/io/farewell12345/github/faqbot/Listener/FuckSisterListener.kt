package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.Python.FuckSchoolSisterUntil
import io.farewell12345.github.faqbot.route.IMessageEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.MiraiInternalApi

class FuckSisterListener : BaseListeners(), IMessageEvent {
    companion object{
        val fuckSchoolSisterUntil = FuckSchoolSisterUntil()
        const val SERVICE_URL = ""
    }
    @EventHandler
    override suspend fun GroupMessageEvent.onEvent(){
        val res = fuckSchoolSisterUntil.verifyMsg(message.content)
        if (res == "fuck_sister"){
            subject.sendMessage("您🐎死了\n")
        }

    }
}