package ink.umb.faqbot.listener

import ink.umb.faqbot.route.IMessageEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent

class DiyServiceListener: BaseListeners(), IMessageEvent {
    @EventHandler
    override suspend fun GroupMessageEvent.onEvent(){

    }
    companion object{

    }
}