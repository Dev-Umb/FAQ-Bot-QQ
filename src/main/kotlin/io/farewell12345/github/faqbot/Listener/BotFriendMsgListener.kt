package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.SessionManager
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import kotlinx.coroutines.ObsoleteCoroutinesApi
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.utils.MiraiInternalApi

class BotFriendMsgListener : BaseListeners() {
    @ObsoleteCoroutinesApi
    @MiraiInternalApi
    @ExperimentalStdlibApi
    @EventHandler
    suspend fun FriendMessageEvent.onEvent() {
        route(prefix = "", delimiter = " ") {
            if (SessionManager.hasSession(sender.id)) {
                if (SessionManager.performSession(this.event)) {
                    subject.sendMessage("录入成功！任务正在处理，请稍等")
                    return@route
                }
                subject.sendMessage("格式有误！请检查录入答案格式")
            }
            case("添加定时任务", "添加定时任务") {
                val Id:Long = commandText.toLong()
                val thisGroup = bot.getGroup(Id)!!
                if((thisGroup.members[sender.id]?.permission?.ordinal == 0)){
                    subject.sendMessage("无权限，你不是目标群管理员")
                    return@route
                }
                SessionManager.addSession(
                    user = event.sender.id,
                    session = Session(
                        user = event.sender.id,
                        type = "timerTask",
                        group = thisGroup
                    )
                )
                subject.sendMessage("请输入定时消息")
            }
        }
    }
}