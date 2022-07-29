package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.Listener.BaseListeners
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent

class NewFriendMember : BaseListeners() {
    @EventHandler
    suspend fun NewFriendRequestEvent.onEvent() {
        this.accept()
    }
    @EventHandler
    suspend fun BotInvitedJoinGroupRequestEvent.onEvent() {
        this.accept()
    }
}