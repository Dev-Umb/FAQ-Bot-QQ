package io.farewell12345.github.faqbot.BotManager

import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
object DisRepetition{
    var groupMap = mutableMapOf<Long, MessageChain>()
    fun thisMessageIsRepetition(msg: GroupMessageEvent):Boolean{
        if (groupMap[msg.group.id]!=null){
            val a = groupMap[msg.group.id]?.get(PlainText).hashCode()+ groupMap[msg.group.id]?.get(Face).hashCode() + groupMap[msg.group.id]?.get(Image).hashCode()+ groupMap[msg.group.id]?.get(At).hashCode()
            val b = msg.message[PlainText].hashCode()+msg.message[Face].hashCode()+msg.message[Image].hashCode()+msg.message[At].hashCode()
            if (a == b){
                groupMap.remove(msg.group.id)
                return true
            }else{
                groupMap[msg.group.id] = msg.message
            }
        }else{
            groupMap[msg.group.id] = msg.message
        }
        return false
    }
}
