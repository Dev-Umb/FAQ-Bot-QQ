package ink.umb.faqbot.listener

import ink.umb.faqbot.bot.manager.CommandGroupList
import ink.umb.faqbot.process.FuckSchoolSisterUtil
import ink.umb.faqbot.route.IMessageEvent
import net.mamoe.mirai.contact.isAdministrator
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.recall

class FuckSisterListener : BaseListeners(), IMessageEvent {
    companion object{
        val fuckSchoolSisterUntil = FuckSchoolSisterUtil
    }
    @EventHandler
    override suspend fun GroupMessageEvent.onEvent(){
        if (group.id !in CommandGroupList.fuckPsSister){
            return
        }
        val res = fuckSchoolSisterUntil.verifyMsg(message.content)
        if (res?.isFake == true){
            subject.sendMessage(
                buildMessageChain {
                    add(PlainText("检测到包含违法信息"))
                    add(At(sender))
                })
            if (group.botPermission.isAdministrator() && !sender.isOperator()){
                try {
                    message.recall()
                }catch (_:Exception){

                }
            }
        }

    }
}