@file:Suppress(
    "EXPERIMENTAL_API_USAGE",
    "DEPRECATION_ERROR",
    "OverridingDeprecatedMember",
    "INVISIBLE_REFERENCE",
    "INVISIBLE_MEMBER"
)
package io.farewell12345.github.faqbot.BotManager
import io.farewell12345.github.faqbot.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext
internal val appJob = Job()

object CommandGroupList {
    lateinit var welcomeGroupList:ArrayList<Long>
    lateinit var managerGroupList:ArrayList<Long>
    lateinit var fakeInfoIdentityHashMap: HashMap<Long, Boolean>
    fun init(){
        fakeInfoIdentityHashMap = HashMap()
        welcomeGroupList = ArrayList()
        managerGroupList = ArrayList()
    }
}




object BotsManager : CoroutineScope,EventListener {
    var oneBot: Bot? = null
    val task = TimerSessionManager()  // 命令调度器
    suspend fun loginBot():Bot {
        oneBot =  BotFactory.newBot(
                qq = AppConfig.getInstance().botQQ.toLong(),
                password = AppConfig.getInstance().botPwd
        ) {
            this.protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE
            val deviceInfoFolder = File("devices")
            if (!deviceInfoFolder.exists()) {
                deviceInfoFolder.mkdir()
            }
            fileBasedDeviceInfo(File(deviceInfoFolder,
                    "${AppConfig.getInstance().botQQ.toLong()}.json").absolutePath)
            botLoggerSupplier = { _ ->
                MiraiLogger.create("Bot")
            }
            networkLoggerSupplier = { _ ->
                MiraiLogger.create("NetWork")
            }
        }
        oneBot?.login()
        return oneBot as Bot
    }
    val jobs = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+ SupervisorJob(appJob) + jobs
    fun closeAllBot() {
        jobs.cancel()
    }
}
