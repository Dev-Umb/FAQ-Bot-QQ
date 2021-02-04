@file:Suppress(
    "EXPERIMENTAL_API_USAGE",
    "DEPRECATION_ERROR",
    "OverridingDeprecatedMember",
    "INVISIBLE_REFERENCE",
    "INVISIBLE_MEMBER"
)
package io.farewell12345.github.faqbot.DTO.model

import io.farewell12345.github.faqbot.DTO.DB.DB
import com.google.gson.Gson
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Welcome
import io.farewell12345.github.faqbot.DTO.model.dataclass.Answer
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiInternalApi
import org.apache.logging.log4j.LogManager.*
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.StackLocatorUtil
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*

fun logger(): Logger {
    return getLogger(StackLocatorUtil.getStackTraceElement(2).className)
}