package umb.ink.ktor.bot.message.listener


import net.mamoe.mirai.event.events.GroupMessageEvent
import umb.ink.ktor.bot.message.BaseListener

class GroupListener: BaseListener<GroupMessageEvent>(INSTANCE) {
    companion object{
        private val INSTANCE = GroupListener()
    }
    fun GroupMessageEvent.onEvent(){

    }
}