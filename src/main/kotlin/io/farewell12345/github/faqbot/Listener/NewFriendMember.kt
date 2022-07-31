package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.Listener.BaseListeners
import io.farewell12345.github.faqbot.route.IMessageEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent

class NewFriendMember : BaseListeners(), IMessageEvent{
    @EventHandler
    override suspend fun NewFriendRequestEvent.onEvent() {
        this.accept()
    }
    @EventHandler
    override suspend fun BotInvitedJoinGroupRequestEvent.onEvent() {
        this.accept()
    }

}