package ink.umb.faqbot.listener

import ink.umb.faqbot.listener.BaseListeners.ListenerManager.Companion.listeners
import ink.umb.faqbot.plugin.ProgramManage
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.SimpleListenerHost
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.StackLocatorUtil
import org.reflections.Reflections
import java.util.LinkedList
import kotlin.coroutines.CoroutineContext

abstract class BaseListeners : SimpleListenerHost() {


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
        protected val logger: Logger
            get() = LogManager.getLogger(StackLocatorUtil.getCallerClass(2).name)
        init {
            val reflections: Reflections = Reflections("ink.umb.faqbot.listener")
            val subTypes: Set<Class<out BaseListeners?>> = reflections.getSubTypesOf(BaseListeners::class.java)
            subTypes.forEach{
                val listener = it.getDeclaredConstructor().newInstance()
                if (listener is BaseListeners) {
                    addListeners(listener)
                }
                logger.debug("loader:$listener")
            }
        }
        fun registerListeners(bot: Bot){
            listeners.forEach {
                bot.eventChannel.registerListenerHost(it)
                logger.debug("bot loader:$it")
            }
        }
        private fun addListeners(listener: BaseListeners) {
            listeners.add(listener)
        }
    }
}

