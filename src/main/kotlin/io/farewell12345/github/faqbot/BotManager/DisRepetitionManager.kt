package io.farewell12345.github.faqbot.BotManager

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
object DisRepetition{
    private var groupMap = mutableMapOf<Long, MessageChain>()
    fun thisMessageIsRepetition(msg: GroupMessageEvent):Boolean{
        if(groupMap[msg.group.id]!=null){
            val a = groupMap[msg.group.id]?.findIsInstance<PlainText>()?:false
            val b = msg.message.findIsInstance<PlainText>()?:false
            if (a == b){
                groupMap.remove(msg.group.id)
                return true
            }
            groupMap[msg.group.id] = msg.message
            return false
        }else{
            groupMap[msg.group.id] = msg.message
            return false
        }
    }
}
