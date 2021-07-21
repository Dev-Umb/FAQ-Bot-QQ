package io.farewell12345.github.faqbot.Plugin.FuckFakeInfo

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
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
            url=AppConfig.getInstance().fakeInfoUrl,
            body="{\"token\":\"$token\",\"text\":\"$text}\",\"account\":\"${message.sender.id}\"}")
        return data
    }
}