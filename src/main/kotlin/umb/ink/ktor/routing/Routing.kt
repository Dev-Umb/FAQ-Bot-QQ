package umb.ink.ktor.routing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.content
import kotlin.coroutines.CoroutineContext

class CommandRouting<T: MessageEvent>(val event: T) : BaseRoute<T>(event), CoroutineScope {

    suspend inline fun case(prefix:String, help:String = "",receiver: T.(List<String>?) -> Unit){
        synchronized(commandIndexList){
            commandIndexList[prefix] = help
        }
        event.receiver(listOf(event.message.content))
    }

    var commandIndexList = HashMap<String,String>()
}

