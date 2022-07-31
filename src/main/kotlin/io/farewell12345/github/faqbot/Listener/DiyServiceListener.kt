package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.route.IMessageEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.MiraiInternalApi

class DiyServiceListener: BaseListeners(), IMessageEvent {
    @EventHandler
    override suspend fun GroupMessageEvent.onEvent(){

    }
    companion object{

    }
}