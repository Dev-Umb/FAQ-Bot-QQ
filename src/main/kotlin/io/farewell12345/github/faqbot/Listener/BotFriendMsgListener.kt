package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.BotManager.PicManager
import io.farewell12345.github.faqbot.BotManager.SessionManager
import io.farewell12345.github.faqbot.DTO.Controller.ForwardController
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import io.farewell12345.github.faqbot.Plugin.FuckNuc.sign
import kotlinx.coroutines.ObsoleteCoroutinesApi
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.AtAll
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import net.mamoe.mirai.utils.MiraiInternalApi

class BotFriendMsgListener : BaseListeners() {
    @ObsoleteCoroutinesApi
    @MiraiInternalApi
    @ExperimentalStdlibApi
    @EventHandler
    suspend fun FriendMessageEvent.onEvent() {
        route {
            if (SessionManager.hasSession(sender.id)) {
                if (SessionManager.performSession(event)) {
                    subject.sendMessage("录入成功！任务正在处理，请稍等")
                    return@route
                }
                subject.sendMessage("格式有误！请检查录入答案格式")
            }
            case("线稿","图片转线稿"){
                if (SessionManager.hasSession(sender.id)) {
                    subject.sendMessage("你有正在进行的会话")
                    return@route
                }
                SessionManager.addSession(
                    user = sender.id,
                    session = Session(
                        user = sender.id,
                        type = "Sobel",
                        group = 0
                    )
                )
                subject.sendMessage("请发送你的图片")
                return@route
            }
            case("添加目标群", canRepetition = false) {
                val forwardGroup = message
                    .firstIsInstanceOrNull<PlainText>()
                    ?.content
                    ?.replace("添加目标群 ", "")
                    ?.toLong()
                if (forwardGroup is Long && bot.getGroup(forwardGroup) != null
                    && bot.getGroup(forwardGroup)?.getMember(sender.id) != null
                ) {
                    ForwardController.addForwardGroup(group = forwardGroup,user = user.id)
                    subject.sendMessage("OK")
                } else {
                    subject.sendMessage("你或者我没加这个群，没法帮你转发")
                }
                return@route
            }
            case("删除目标群", canRepetition = false) {
                val forwardGroup = message
                    .firstIsInstanceOrNull<PlainText>()
                    ?.content
                    ?.replace("删除目标群 ", "")
                    ?.toLong()
                if (forwardGroup is Long && bot.getGroup(forwardGroup) != null) {
                    ForwardController.deleteForwardGroup(group = forwardGroup,user = user.id)
                    subject.sendMessage("OK")
                }
                return@route
            }
            case("涩图来", "ST") {
                PicManager.stImgSend(subject, event)
                return@route
            }
            case("签到") {
                try {
                    commandText.toLong()
                } catch (e: NumberFormatException) {
                    subject.sendMessage("爪巴")
                    return@route
                }
                subject.sendMessage(sign(commandText))
            }
            case("图来", "二次元图") {
                PicManager.imgSend(subject, event)
                return@route
            }
            case("帮助", "获取帮助指令") {
                subject.sendMessage(getHelp())
                return@route
            }
            val forwardList = ForwardController.getForwardList(user.id)
            var num = 0
            if (forwardList.isNotEmpty()) {
                forwardList.forEach {
                    if (CommandGroupList.forwardMessageGroup[it] == false){
                        subject.sendMessage("群$it 已禁止转发消息")
                        return@forEach
                    }
                    val group = bot.getGroup(it)
                    ++num
                    try {
                        group?.sendMessage(message)
                        group?.checkBotPermission(MemberPermission.ADMINISTRATOR)
                        if (message.firstIsInstanceOrNull<PlainText>()?.content?.split(":")?.get(0)!! == "通知") {
                            group?.sendMessage(AtAll)
                        }
                    }catch (e: PermissionDeniedException){

                    }catch (e:NullPointerException){

                    }
                }
                subject.sendMessage("已转发到$num 个群")
                return@route
            }
            throw Exception("您还没有添加需要转发的群")
        }
    }
}