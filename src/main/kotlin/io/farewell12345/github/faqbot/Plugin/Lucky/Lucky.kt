package io.farewell12345.github.faqbot.Plugin.Lucky

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import java.util.*
import kotlin.math.abs

object Lucky {
    fun getDraw(sender: Member, things: MessageChain?): String {
        val key = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val flag = (key xor sender.nameCard.hashCode()) %
                (AppConfig.getInstance().badOrGood.size)
        var num = abs(sender.id.hashCode() xor key)
        things?.forEach {
            num = num xor it.hashCode()
        }
        num %= (AppConfig.getInstance().draws.size)
        return AppConfig.getInstance().draws[abs(num)] +
                AppConfig.getInstance().badOrGood[abs(flag)]
    }
    fun getTodayLucky(){

    }
}