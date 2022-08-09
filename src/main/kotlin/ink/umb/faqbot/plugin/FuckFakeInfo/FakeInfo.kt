package ink.umb.faqbot.plugin.FuckFakeInfo

import ink.umb.faqbot.AppConfig
import ink.umb.faqbot.http.FuckOkhttp
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.findIsInstance

object FakeInfo {
    private val token: String = AppConfig.getInstance().fakeInfoKey
    fun verifyMessage(message: GroupMessageEvent): FuckOkhttp.VerifyBaseModel {
        val text = message.message.findIsInstance<PlainText>()
        return FuckOkhttp().post(
            url = AppConfig.getInstance().fakeInfoUrl,
            body = "{\"token\":\"$token\",\"text\":\"$text}\",\"account\":\"${message.sender.id}\"}"
        )
    }
}