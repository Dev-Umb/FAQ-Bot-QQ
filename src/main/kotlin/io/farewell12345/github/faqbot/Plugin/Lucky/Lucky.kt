package io.farewell12345.github.faqbot.Plugin.Lucky

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import java.util.*

object Lucky {
    fun getDraw(userId: Long, things: String?): String? {

        val key = Calendar.getInstance().get(Calendar.MONTH) and
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) xor
                BotsManager.oneBot?.bot.hashCode()
        var flag = (key xor userId.toInt()) %
                (AppConfig.getInstance().badOrGood.size - 1)
        val num = Math.abs(userId.toInt() and things.hashCode() xor key) %
                (AppConfig.getInstance().draws.size - 1)
        if (things?.get(0) ?: "" in arrayListOf('不', '没', '否')) {
            things?.removeRange(0, 0)
            flag = Math.abs(flag - 1)
        }
        return AppConfig.getInstance().draws[num] +
                AppConfig.getInstance().badOrGood[flag]
    }
}