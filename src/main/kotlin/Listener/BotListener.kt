package Listener

import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.StackLocatorUtil
import javax.sound.midi.Receiver
import kotlin.coroutines.CoroutineContext

abstract class BaseListeners : SimpleListenerHost() {
    protected val logger: Logger
        get() = LogManager.getLogger(StackLocatorUtil.getCallerClass(2).name)


    override fun handleException(context: CoroutineContext, exception: Throwable) {
        logger.error(exception)
    }

    companion object {
        val listeners = listOf(
                BotMsgListener()
        )
    }
}

