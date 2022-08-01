package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.Python.FuckSchoolSisterUntil
import io.farewell12345.github.faqbot.route.IMessageEvent
import net.mamoe.mirai.contact.isAdministrator
import net.mamoe.mirai.contact.isBotMuted
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.utils.MiraiInternalApi

class FuckSisterListener : BaseListeners(), IMessageEvent {
    companion object{
        val fuckSchoolSisterUntil = FuckSchoolSisterUntil
    }
    @EventHandler
    override suspend fun GroupMessageEvent.onEvent(){
        val res = fuckSchoolSisterUntil.verifyMsg(message.content)
        if (res?.isFake == true){
            subject.sendMessage(
                buildMessageChain {
                    add(PlainText("检测到包含违法信息"))
                    add(At(sender))
                })
            if (group.botPermission.isAdministrator() && sender.isOperator()){
                try {
                    message.recall()
                }catch (_:Exception){

                }
            }
        }

    }
}