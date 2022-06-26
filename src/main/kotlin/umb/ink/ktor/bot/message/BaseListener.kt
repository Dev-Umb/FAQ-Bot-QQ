package umb.ink.ktor.bot.message

import io.netty.util.internal.shaded.org.jctools.queues.SpscLinkedQueue
import net.mamoe.mirai.event.SimpleListenerHost
import java.util.LinkedList

abstract class BaseListener<T>(child: SimpleListenerHost) : SimpleListenerHost() {
    companion object{
        val listeners:LinkedList<SimpleListenerHost> = LinkedList()
    }

    protected val messageQueue = SpscLinkedQueue<T>()

    init {
        listeners.add(child)
    }

    override fun handleException(context: kotlin.coroutines.CoroutineContext, exception: Throwable) {
        super.handleException(context, exception)
    }
}