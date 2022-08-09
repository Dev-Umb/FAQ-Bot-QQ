package ink.umb.faqbot.listener

import ink.umb.faqbot.route.IMessageEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
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