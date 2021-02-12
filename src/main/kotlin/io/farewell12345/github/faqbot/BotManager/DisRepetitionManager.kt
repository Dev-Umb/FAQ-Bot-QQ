package io.farewell12345.github.faqbot.BotManager

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
object DisRepetition{
    private var groupMap = mutableMapOf<Long, MessageChain>()
    fun thisMessageIsRepetition(msg: GroupMessageEvent):Boolean{
        if (groupMap[msg.group.id]!=null){
            val a = buildString {
                groupMap[msg.group.id]?.forEach {
                    append(it.contentToString())
                }
            }
            val b = buildString {
                msg.message.forEach {
                    append(it.contentToString())
                }
            }
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
