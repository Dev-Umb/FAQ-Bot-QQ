package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.DTO.model.logger
import io.farewell12345.github.faqbot.Listener.BaseListeners.ListenerManager.Companion.listeners
import io.farewell12345.github.faqbot.Plugin.ProgramManage
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.GroupMessageEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.StackLocatorUtil
import org.reflections.Reflections
import java.util.LinkedList
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
    private class ListenerManager{
        companion object{
            var listeners: LinkedList<BaseListeners> = LinkedList()
        }
    }
    companion object {
        init {
            val reflections: Reflections = Reflections("io.farewell12345.github.faqbot.Listener")
            val subTypes: Set<Class<out BaseListeners?>> = reflections.getSubTypesOf(BaseListeners::class.java)
            subTypes.forEach{
                val listener = it.getDeclaredConstructor().newInstance()
                if (listener is BaseListeners) {
                    addListeners(listener)
                }
                logger().debug("loader:$listener")
            }
        }
        fun registerListeners(bot: Bot){
            listeners.forEach {
                bot.eventChannel.registerListenerHost(it)
                logger().debug("bot loader:$it")
            }
        }
        private fun addListeners(listener: BaseListeners) {
            listeners.add(listener)
        }
    }
}

