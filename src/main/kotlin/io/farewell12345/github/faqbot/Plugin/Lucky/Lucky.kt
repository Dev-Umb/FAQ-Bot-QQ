package io.farewell12345.github.faqbot.Plugin.Lucky

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import java.util.*

object Lucky {
    private val date = CommandGroupList.calendar.get(Calendar.MONTH) and
            CommandGroupList.calendar.get(Calendar.DAY_OF_MONTH)
    fun getLuckyOrUnLucky(userId:Long,things:String?):String{
        return AppConfig.getInstance()
            .luckyOrUnLucky[(((userId.toInt() and date) xor things.hashCode()) % 2)]
    }

    fun getDraw(userId:Long,things:String?): String? {
        return AppConfig.getInstance()
            .draws[(userId.toInt() and things.hashCode() and date) % 3]
    }
}