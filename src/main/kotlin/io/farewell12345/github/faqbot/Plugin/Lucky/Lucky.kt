package io.farewell12345.github.faqbot.Plugin.Lucky

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.*
import java.util.*
import kotlin.math.abs

object Lucky {
    var badOrGood = arrayOf("凶", "吉")
    var draws = arrayOf("大", "中", "小")
    fun getDraw(sender: Member, things: MessageChain?): String {
        val key = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val flag = (key xor sender.nameCard.hashCode()) %
                (badOrGood.size)
        var num = abs(sender.id.hashCode() xor key)
        num = num xor things?.findIsInstance<PlainText>().toString().hashCode()
        num %= (draws.size)
        return draws[abs(num)] +
                badOrGood[abs(flag)]
    }
    fun getTodayLucky(){

    }
}