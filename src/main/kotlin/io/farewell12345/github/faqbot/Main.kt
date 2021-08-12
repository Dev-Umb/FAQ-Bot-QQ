@file:Suppress(
        "EXPERIMENTAL_API_USAGE",
        "DEPRECATION_ERROR",
        "OverridingDeprecatedMember",
        "INVISIBLE_REFERENCE",
        "INVISIBLE_MEMBER"
)

package io.farewell12345.github.faqbot

import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.DTO.DB.DB
import io.farewell12345.github.faqbot.Listener.BaseListeners
import io.farewell12345.github.faqbot.DTO.model.logger
import io.farewell12345.github.faqbot.Plugin.ProgramManage
import kotlinx.coroutines.joinAll

suspend fun main() {
    // 添加监听job
    DB
    ProgramManage.run()
}