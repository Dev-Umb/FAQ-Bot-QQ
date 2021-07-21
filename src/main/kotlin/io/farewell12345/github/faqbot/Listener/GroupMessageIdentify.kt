package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.Plugin.FuckFakeInfo.FakeInfo
import net.mamoe.mirai.contact.PermissionDeniedException
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.utils.MiraiInternalApi

class GroupMessageIdentify : BaseListeners()  {
    @MiraiInternalApi
    @EventHandler
    suspend fun GroupMessageEvent.onEvent(){
        if(CommandGroupList.fakeInfoIdentityHashMap[group.id] != true){
            return
        }
        try {

            val verifyResult = FakeInfo.verifyMessage(this)
            if (verifyResult.isFake) {
                subject.sendMessage(buildMessageChain {
                    append(At(sender.id))
                    append(PlainText("言论包含非法词汇${
                        buildString { verifyResult.data?.forEach {
                            if (it.value is List<*>){
                                if((it.value as List<*>).size > 0){
                                    append("[")
                                    append(it.key+"")
                                    append("]")
                                }
                            }
                        } }}！"))
                })
                try {
                    message.recall()
                }catch(e: PermissionDeniedException){

                }
            }
        }catch(e:NullPointerException){

        }
    }
}