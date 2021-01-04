@file:Suppress(
    "EXPERIMENTAL_API_USAGE",
    "DEPRECATION_ERROR",
    "OverridingDeprecatedMember",
    "INVISIBLE_REFERENCE",
    "INVISIBLE_MEMBER"
)
package io.farewell12345.github.faqbot.BotManager
import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.Task.GameMorningTask
import io.farewell12345.github.faqbot.Task.TimerSessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.utils.internal.logging.Log4jLogger
import org.apache.logging.log4j.LogManager
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext
internal val appJob = Job()

object CommandGroupList {
    lateinit var oneBot: Bot
    lateinit var welcomeGroupList:LinkedList<Long>
    lateinit var managerGroupList:LinkedList<Long>
    lateinit var GameMorningGroupList:LinkedList<Long>
    lateinit var DisRepetitionGroupList:LinkedList<Long>
    lateinit var AnimationGroupList:LinkedList<Long>
}



object BotsManager : CoroutineScope {
    val task = TimerSessionManager()  // 命令调度器
    suspend fun loginBot(): Bot {
        return Bot(
                qq = AppConfig.getInstance().BotQQ.toLong(),
                password = AppConfig.getInstance().BotPwd
        ) {
            val deviceInfoFolder = File("devices")
            if (!deviceInfoFolder.exists()) {
                deviceInfoFolder.mkdir()
            }
            fileBasedDeviceInfo(File(deviceInfoFolder,
                    "${AppConfig.getInstance().BotQQ.toLong()}.json").absolutePath)
            botLoggerSupplier = { _ ->
                Log4jLogger(LogManager.getLogger("BOT"))
            }
            networkLoggerSupplier = { _ ->
                Log4jLogger(LogManager.getLogger("NETWORK"))
            }
        }.alsoLogin()
    }
    val jobs = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+ SupervisorJob(appJob) + jobs
    fun closeAllBot() {
        jobs.cancel()
    }
}
