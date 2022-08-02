package io.farewell12345.github.faqbot.Python

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.DTO.model.dataclass.FuckSisterResponse
import io.farewell12345.github.faqbot.DTO.model.logger
import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import java.net.ConnectException


@OptIn(DelicateCoroutinesApi::class)
object FuckSchoolSisterUntil {
    private var PREDICT_PYTHON_URI = AppConfig.getInstance().predictPyUri
    private var process: Process? = null
    private const val predictUrl = "http://127.0.0.1:9001/predict"
    fun destroy(){
        if (process?.isAlive == true){
            process?.destroy()
        }
    }
    fun restart(){
        destroy()
        start()
    }
    private fun start(){
        process = Runtime.getRuntime().exec("python3 $PREDICT_PYTHON_URI")
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                println("start sever")
                while (process?.isAlive == true){
                    try{
                        FuckOkhttp("http://127.0.0.1:9001/heart").get()
                    }catch (e: ConnectException){
                        logger().debug("connect failed try to reconnect")
                    }catch (e: java.net.SocketTimeoutException){
                        logger().debug("connect timeout")
                    }catch (e: Exception){
                        logger().debug(e)
                    }
                    Thread.sleep(1000)
                }
                logger().debug("process is exit!!,try to restart")
                restart()
            }
        }
    }
    init {
       start()
    }
    public fun verifyMsg(text: String): FuckSisterResponse? {
        return try {
            FuckOkhttp(predictUrl).postFuckSister(text)
        }catch (e: Exception){
            null
        }
    }
    public fun test(): String? {
        return FuckOkhttp("http://127.0.0.1:9001/").get()
    }
}

fun main() {
//    val res = FuckSchoolSisterUntil.test()
    try {
        val res = FuckSchoolSisterUntil.verifyMsg("大家有需要考研公共课书的加我哦，质量好价格贼实惠，时不时还有考研小福利的哦  有需要的加15536840488\n")
        println(res)
    }catch (e:Exception){
        FuckSchoolSisterUntil.destroy()
        throw e
    }
    FuckSchoolSisterUntil.destroy()
}