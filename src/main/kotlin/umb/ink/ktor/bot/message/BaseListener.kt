package umb.ink.ktor.bot.message

import net.mamoe.mirai.event.SimpleListenerHost
import java.util.LinkedList
import java.util.concurrent.LinkedBlockingQueue

abstract class BaseListener<T>(child: SimpleListenerHost) : SimpleListenerHost() {
    companion object{
        val listeners:LinkedList<SimpleListenerHost> = LinkedList()
    }

    protected val messageQueue = LinkedBlockingQueue<T>()

    init {
        listeners.add(child)
    }

    override fun handleException(context: kotlin.coroutines.CoroutineContext, exception: Throwable) {
        super.handleException(context, exception)
    }
}