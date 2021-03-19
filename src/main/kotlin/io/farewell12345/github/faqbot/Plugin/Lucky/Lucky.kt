package io.farewell12345.github.faqbot.Plugin.Lucky

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import java.util.*

object Lucky {
    fun getDraw(userId:Long,things:String?): String? {
        val key = Calendar.getInstance().get(Calendar.MONTH) and
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) xor
                BotsManager.oneBot?.bot.hashCode()
        return AppConfig.getInstance()
            .draws[Math.abs(userId.toInt() and things.hashCode() xor key) %
                (AppConfig.getInstance().draws.size - 1) ]
    }
}