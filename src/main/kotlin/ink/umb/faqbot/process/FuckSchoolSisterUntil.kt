package ink.umb.faqbot.process

import ink.umb.faqbot.AppConfig
import ink.umb.faqbot.dto.db.logger
import ink.umb.faqbot.dto.model.dataclass.FuckSisterResponse
import ink.umb.faqbot.fuck.http.FuckOkhttp
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ConnectException


@OptIn(DelicateCoroutinesApi::class)
object FuckSchoolSisterUntil {
    private val log = logger()
    private var PREDICT_PYTHON_URI = AppConfig.getInstance().predictPyUri
    private var process: Process? = null
    private const val predictUrl = "http://127.0.0.1:9001/predict"
    fun destroy(){
        if (process?.isAlive == true){
            process?.destroy()
        }
    }
    fun restart(){
        log.debug("restart service")
        destroy()
        start()
    }
    private suspend fun extractProcessStream() = GlobalScope.launch{
        val br = process?.inputStream?.let { InputStreamReader(it) }?.let { BufferedReader(it) }
        val errBr = process?.errorStream?.let { InputStreamReader(it) }?.let { BufferedReader(it) }
        var output: String? = null
        var errOutput: String? = null
        while (null != withContext(Dispatchers.IO) {
                br!!.readLine()
            }.also { output = it }) {
            errOutput = withContext(Dispatchers.IO) {
                errBr!!.readLine()
            }
            log.debug(output)
            log.debug(errOutput)
        }
        withContext(Dispatchers.IO) {
            process?.waitFor()
            br?.close()
            errBr?.close()
        }
    }
    private fun heart(){
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                log.debug("service heart listener")
                while (true){
                    if (process?.isAlive == false){
                        log.debug("process is exit!!,try to restart")
                        restart()
                    }
                    try{
                        FuckOkhttp("http://127.0.0.1:9001/heart").get()
                    }catch (e: ConnectException){
                        log.debug("connect failed try to reconnect")
                    }catch (e: java.net.SocketTimeoutException){
                        log.debug("connect timeout, try to reconnect")
                        for (i in 0..3){
                            try {
                                val res = FuckOkhttp("http://127.0.0.1:9001/heart").get()
                                if (res == "success"){
                                    break
                                }
                            }catch (_: Exception){

                            }
                        }
                        restart()
                    }catch (e: Exception){
                        log.debug(e)
                        restart()
                    }
                    Thread.sleep(2000)
                }
            }
        }
    }
    private fun start(){
        process = Runtime.getRuntime().exec("python3 $PREDICT_PYTHON_URI")
        GlobalScope.launch {
            extractProcessStream()
        }
    }
    private fun init(){
        start()
        Thread {
            heart()
        }.start()
    }
    init {
       init()
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
//    try {
//        val res = FuckSchoolSisterUntil.verifyMsg("大家有需要考研公共课书的加我哦，质量好价格贼实惠，时不时还有考研小福利的哦  有需要的加15536840488\n")
//        println(res)
//    }catch (e:Exception){
//        FuckSchoolSisterUntil.destroy()
//        throw e
//    }
    FuckSchoolSisterUntil
    while (true){
    }
    FuckSchoolSisterUntil.destroy()
}