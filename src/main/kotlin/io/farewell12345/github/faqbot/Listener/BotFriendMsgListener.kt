package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.BotManager.PicManager
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.FriendMessageEvent

class BotFriendMsgListener:BaseListeners() {
    @EventHandler
    suspend fun FriendMessageEvent.onEvent() {
        route {
            case("涩图来", "ST") {
                PicManager.stImgSend(subject,event)
                return@route
            }
            case("图来", "二次元图") {
                PicManager.imgSend(subject,event)
                return@route
            }
        }
    }
}