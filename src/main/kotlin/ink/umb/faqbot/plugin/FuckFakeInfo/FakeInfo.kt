package ink.umb.faqbot.plugin.FuckFakeInfo

import ink.umb.faqbot.AppConfig
import ink.umb.faqbot.fuck.http.FuckOkhttp
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.findIsInstance

object FakeInfo {
    val token = AppConfig.getInstance().fakeInfoKey
    fun verifyMessage(message:GroupMessageEvent): FuckOkhttp.VerifyBaseModel {
        if (token ==null){
            throw NullPointerException("此bot暂未接入该服务")
        }
        val text = message.message.findIsInstance<PlainText>()
        val data = FuckOkhttp().post<FuckOkhttp.VerifyBaseModel>(
            url= AppConfig.getInstance().fakeInfoUrl,
            body="{\"token\":\"$token\",\"text\":\"$text}\",\"account\":\"${message.sender.id}\"}")
        return data
    }
}