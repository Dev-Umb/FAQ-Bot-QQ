package io.farewell12345.github.faqbot.Listener
import com.google.gson.Gson
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.DTO.Answer
import io.farewell12345.github.faqbot.DTO.Question
import io.farewell12345.github.faqbot.curd.searchWelcomeTalk
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText

class NewGroupMemberListener : BaseListeners() {
    @EventHandler
    suspend fun MemberJoinEvent.onEvent() {
        val user = this.member
        val groupId = this.group.id
        if (groupId in CommandGroupList.welcomeGroupList) {
            val gson = Gson()
            val talk = gson.fromJson(searchWelcomeTalk(group),Answer::class.java)
            val messageChain = MessageChainBuilder()
            messageChain.add(At(user))
            messageChain.add(talk.text)
            talk.atList.forEach {
                messageChain.add(
                    At(Bot.botInstances[0]
                        .getGroup(group.id)[it])
                )
            }
            talk.imgList.forEach {
                messageChain.add(Image(it))
            }
            CommandGroupList.oneBot.getGroup(groupId)
                .sendMessage(
                    messageChain.build()
                )
        }
    }
}