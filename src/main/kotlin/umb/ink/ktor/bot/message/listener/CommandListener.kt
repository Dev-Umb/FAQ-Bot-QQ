package umb.ink.ktor.bot.message.listener

import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.MiraiInternalApi
import umb.ink.ktor.bot.message.BaseListener
import umb.ink.ktor.bot.message.bean.isCommandEvent
import umb.ink.ktor.routing.CommandRouting
import umb.ink.ktor.routing.until.CommandPermissions

class CommandListener:BaseListener<GroupMessageEvent>(INSTANCE) {
    companion object{
        private val INSTANCE = CommandListener()
    }
    @MiraiInternalApi
    @EventHandler
    suspend fun GroupMessageEvent.onEvent(){
        if (!this.isCommandEvent())return

    }
}