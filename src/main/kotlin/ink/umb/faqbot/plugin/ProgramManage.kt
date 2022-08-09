package ink.umb.faqbot.plugin

import ink.umb.faqbot.bot.manager.BotsManager
import ink.umb.faqbot.bot.manager.CommandGroupList
import ink.umb.faqbot.dto.db.logger
import ink.umb.faqbot.listener.BaseListeners
import kotlinx.coroutines.joinAll

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