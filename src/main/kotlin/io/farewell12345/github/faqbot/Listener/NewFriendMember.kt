package io.farewell12345.github.faqbot.Listener

import com.google.gson.Gson
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.DTO.Answer
import io.farewell12345.github.faqbot.curd.searchWelcomeTalk
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.FriendAddEvent
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageChainBuilder

class NewFriendMember : BaseListeners() {
    @EventHandler
    suspend fun NewFriendRequestEvent.onEvent() {
        this.accept()
    }
}