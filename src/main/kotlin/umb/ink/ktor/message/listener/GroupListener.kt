package umb.ink.ktor.message.listener

import net.mamoe.mirai.event.events.GroupMessageEvent
import umb.ink.ktor.message.BaseListener

class GroupListener: BaseListener(this.INSTANCE) {
    companion object{
        val INSTANCE = GroupListener()
    }
    fun GroupMessageEvent.onEvent(){

    }
}