package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.Plugin.ProgramManage
import io.farewell12345.github.faqbot.route.BotFriendMsgListener
import io.farewell12345.github.faqbot.route.BotGroupMsgListener
import io.farewell12345.github.faqbot.route.NewFriendMember
import io.farewell12345.github.faqbot.route.NewGroupMemberListener
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.SimpleListenerHost
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.StackLocatorUtil
import kotlin.coroutines.CoroutineContext

abstract class BaseListeners : SimpleListenerHost() {
    protected val logger: Logger
        get() = LogManager.getLogger(StackLocatorUtil.getCallerClass(2).name)

    // 打印错误日志
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        logger.error(exception)
        launch{
            ProgramManage.restart()
        }
    }

    companion object {
        val listeners = listOf(
            BotGroupCommandListener(),
            BotFriendMsgListener(),
            BotGroupMsgListener(),
            NewGroupMemberListener(),
            NewFriendMember(),
            GroupMessageIdentify()
        )
    }
}

