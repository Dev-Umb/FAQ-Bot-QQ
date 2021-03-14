package io.farewell12345.github.faqbot.Plugin.Lucky

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import java.util.*

object Lucky {
    private val key = CommandGroupList.calendar.get(Calendar.MONTH) and
            CommandGroupList.calendar.get(Calendar.DAY_OF_MONTH) xor
            BotsManager.oneBot?.bot.hashCode()

    fun getDraw(userId:Long,things:String?): String? {

        return AppConfig.getInstance()
            .draws[Math.abs(userId.toInt() and things.hashCode() xor key) %
                (AppConfig.getInstance().draws.size - 1) ]
    }
}