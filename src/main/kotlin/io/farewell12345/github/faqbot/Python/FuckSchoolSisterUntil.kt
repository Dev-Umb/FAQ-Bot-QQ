package io.farewell12345.github.faqbot.Python

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.DTO.model.dataclass.FuckSisterResponse
import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import kotlinx.coroutines.*


@OptIn(DelicateCoroutinesApi::class)
object FuckSchoolSisterUntil {
    private var PREDICT_PYTHON_URI = AppConfig.getInstance().predictPyUri
    private var process: Process? = null
    private const val predictUrl = "http://localhost:9001/predict"
    fun destroy(){
        process?.destroy()
    }
    init {
        process = Runtime.getRuntime().exec("python $PREDICT_PYTHON_URI")
        GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    println("start sever")
                    process?.waitFor()
                }
            }
        }
    public fun verifyMsg(text: String): FuckSisterResponse? {
        return FuckOkhttp(predictUrl).postFuckSister(text)
    }
    public fun test(): String? {
        return FuckOkhttp("http://localhost:9001/").get()
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