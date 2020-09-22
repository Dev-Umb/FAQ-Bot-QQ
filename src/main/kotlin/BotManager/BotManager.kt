@file:Suppress(
    "EXPERIMENTAL_API_USAGE",
    "DEPRECATION_ERROR",
    "OverridingDeprecatedMember",
    "INVISIBLE_REFERENCE",
    "INVISIBLE_MEMBER"
)
package BotManager
import DTO.Answer
import DTO.Question
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.internal.logging.Log4jLogger
import org.apache.logging.log4j.LogManager
import java.io.File
import java.lang.Exception
import java.net.URL
import java.util.*
import kotlin.coroutines.CoroutineContext
internal val appJob = Job()
object BotsManager : CoroutineScope {
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
