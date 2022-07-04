package umb.ink.ktor.routing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent
import umb.ink.ktor.bot.message.BaseListener
import kotlin.coroutines.CoroutineContext

open class BaseRoute<T: MessageEvent>(event: T): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + Job()

    companion object{
        val commandRouteList = HashMap<String,Int>()
        suspend inline fun <reified T: MessageEvent> T.route(prefix: String, splitTxt:String = " ", permission: Int, crossinline receiver: suspend BaseRoute<T>.() -> Unit){
            commandRouteList[prefix] = permission
            if (this.message.contentToString().startsWith(prefix)){
                return
            }
            val router = BaseRoute(this)
            router.launch {
                receiver(router)
            }
        }
    }
}

