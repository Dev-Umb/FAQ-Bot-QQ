package ink.umb.faqbot.route

import net.mamoe.mirai.event.events.*

interface IMessageEvent {
    suspend fun GroupMessageEvent.onEvent(){}
    suspend fun FriendMessageEvent.onEvent(){}
    suspend fun BotInvitedJoinGroupRequestEvent.onEvent(){}
    suspend fun MemberJoinEvent.onEvent(){}
    suspend fun NewFriendRequestEvent.onEvent(){}
}