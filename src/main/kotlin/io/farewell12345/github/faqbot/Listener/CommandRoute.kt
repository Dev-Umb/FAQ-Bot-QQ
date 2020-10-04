package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.curd.logger
import kotlinx.coroutines.*
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Message
import kotlin.coroutines.CoroutineContext

val unCompleteValue = hashMapOf<Long, CompletableDeferred<String>>()
class CommandRoute<T : MessageEvent>( val args: List<String>?,  val event: T) : CoroutineScope {
    val helpMap = hashMapOf<String,String>()
    var alreadyCalled = false
    val job = Job()
    private val  logger = logger()
    var errHandler: (suspend (Throwable) -> Message?)? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    suspend inline fun case(case: String, desc: String = "暂无描述", receiver:  T.(List<String>?) -> Unit) {
        helpMap[case]=desc
        if (args?.size == 0){
            return
        }
        if (!alreadyCalled && case == args?.get(0) ) {
            alreadyCalled = true
            kotlin.runCatching { event.receiver(args.subList(1, args.size)) }.also {
                handleException(it.exceptionOrNull())
            }
        }
    }

    suspend inline fun furry(case: String, desc: String = "暂无描述", receiver:  T.(List<String>?) -> Unit) {
        helpMap[case]=desc
        if (args?.size == 0){
            return
        }
        val pattern = Regex(args.toString())
        val test = pattern.find(case)
        if (!alreadyCalled && pattern.find(case)!=null) {
            alreadyCalled = true
            kotlin.runCatching { event.receiver(args?.subList(1, args.size)) }.also {
                handleException(it.exceptionOrNull())
            }
        }
    }

    suspend fun handleException(throwable: Throwable?) {
        logger.error(throwable?:return)
        val repMsg = errHandler?.let { it1 -> it1(throwable) }
        event.reply(repMsg ?: return)
    }

    fun getHelp():String = buildString{
        append("指令帮助\n")
        append(helpMap.asSequence().joinToString(separator = "\n") { "${it.key} : ${it.value}" })
    }
}

inline fun <reified T : MessageEvent> T.route(
        prefix: String = "",
        delimiter: String = " ",
        crossinline receiver: suspend CommandRoute<T>.() -> Unit
): Boolean {
    val msg = this.message.contentToString()
    if (unCompleteValue.containsKey(this.sender.id)) {
        unCompleteValue[this.sender.id]?.complete(msg)
        return true
    }

    if (!msg.startsWith(prefix)) {
        return false
    }
    val args = msg.split(delimiter)
    val router = CommandRoute(args, this)
    router.launch { receiver(router) }
    return true
}