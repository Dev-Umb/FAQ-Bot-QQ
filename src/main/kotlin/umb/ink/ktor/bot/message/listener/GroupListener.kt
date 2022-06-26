package umb.ink.ktor.bot.message.listener

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.routing.*
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.MiraiInternalApi
import umb.ink.ktor.bot.message.BaseListener

class GroupListener: BaseListener<GroupMessageEvent>(INSTANCE) {
    companion object{
        private val INSTANCE = GroupListener()
    }
    fun GroupMessageEvent.onEvent(){

    }
}