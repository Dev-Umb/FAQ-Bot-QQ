package io.farewell12345.github.faqbot.Plugin

import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.DTO.model.logger
import io.farewell12345.github.faqbot.Listener.BaseListeners
import kotlinx.coroutines.cancel
import kotlinx.coroutines.joinAll
import net.mamoe.mirai.alsoLogin

object ProgramManage {
    suspend fun run(){
        val bot = BotsManager.loginBot()  //登录bot
        BaseListeners.registerListeners(bot)
    //    PicManager
        CommandGroupList.init()
        val logger = logger() // 打印日志
        joinAll(BotsManager.jobs) // 将BotsManager的监听事件加入协程
    }
    suspend fun restart(){
        BotsManager.oneBot?.bot?.close()
        run()
    }
}