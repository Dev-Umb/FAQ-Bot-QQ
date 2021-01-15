package io.farewell12345.github.faqbot.BotManager

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
object DisRepetition{
    var groupMap = mutableMapOf<Long, MessageChain>()
    fun thisMessageIsRepetition(msg: GroupMessageEvent):Boolean{
        if (groupMap[msg.group.id]!=null){
            val a = groupMap[msg.group.id]?.filterIsInstance<PlainText>().hashCode()+
                    groupMap[msg.group.id]?.filterIsInstance<Face>().hashCode()+
                    groupMap[msg.group.id]?.filterIsInstance<Image>().hashCode()+
                    groupMap[msg.group.id]?.filterIsInstance<At>().hashCode()
            val b = msg.message.filterIsInstance<PlainText>().hashCode()+
                    msg.message.filterIsInstance<Face>().hashCode()+
                    msg.message[Image].hashCode()+
                    msg.message.filterIsInstance<At>().hashCode()
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
