package io.farewell12345.github.faqbot.Listener

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
    }
    companion object {
        val listeners = listOf(
                BotMsgListener(),
            NewGroupMemberListener()
        )
    }
}

