package ink.umb.faqbot.listener
import com.google.gson.Gson
import ink.umb.faqbot.bot.manager.BotsManager
import ink.umb.faqbot.bot.manager.CommandGroupList
import ink.umb.faqbot.controller.WelcomeController
import ink.umb.faqbot.dto.model.dataclass.Answer
import ink.umb.faqbot.route.IMessageEvent
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildMessageChain

class NewGroupMemberListener : BaseListeners(), IMessageEvent {
    @EventHandler
    override suspend fun MemberJoinEvent.onEvent() {
        val user = this.member
        val groupId = this.group.id
        if (groupId in CommandGroupList.welcomeGroupList) {
            val gson = Gson()
            val talk = gson.fromJson(WelcomeController.searchWelcomeTalk(group), Answer::class.java)
            val messageChain = buildMessageChain {
                +At(user)
                talk.atList.forEach {
                    append(At(Bot.instances[0].getGroup(group.id)?.get(it)!!))
                }
                talk.imgList.forEach {
                    +Image(it)
                }
                +PlainText(talk.text)
            }

            BotsManager.oneBot?.getGroup(groupId)!!.sendMessage(messageChain)
        }
    }
}