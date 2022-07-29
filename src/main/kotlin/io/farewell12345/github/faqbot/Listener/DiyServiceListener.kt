package io.farewell12345.github.faqbot.Listener

import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.MiraiInternalApi

class DiyServiceListener {
    companion object{
        @MiraiInternalApi
        @EventHandler
        suspend fun GroupMessageEvent.onEvent(){

        }
    }
}