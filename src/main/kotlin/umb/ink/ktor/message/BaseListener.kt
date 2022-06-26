package umb.ink.ktor.message

import net.mamoe.mirai.event.SimpleListenerHost
import java.util.LinkedList

open class BaseListener(): SimpleListenerHost() {
    companion object{
        val listeners:LinkedList<SimpleListenerHost> = LinkedList()
    }
    constructor(child: SimpleListenerHost) : this() {
        listeners.add(child)
    }
    override fun handleException(context: kotlin.coroutines.CoroutineContext, exception: Throwable) {
        super.handleException(context, exception)
    }
}