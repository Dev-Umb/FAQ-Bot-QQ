package io.farewell12345.github.faqbot.Listener

import kotlinx.coroutines.ObsoleteCoroutinesApi
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.utils.MiraiInternalApi

class BotFriendMsgListener : BaseListeners() {
    @ObsoleteCoroutinesApi
    @MiraiInternalApi
    @ExperimentalStdlibApi
    @EventHandler
    suspend fun FriendMessageEvent.onEvent() {

    }
}