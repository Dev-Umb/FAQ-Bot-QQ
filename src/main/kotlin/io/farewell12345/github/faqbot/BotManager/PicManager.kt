package io.farewell12345.github.faqbot.BotManager

import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.DTO.model.Pic
import io.farewell12345.github.faqbot.DTO.model.SexImg
import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp

object PicManager{
    private var imgurl = GsonBuilder()
            .create().fromJson(
                    FuckOkhttp("https://tenapi.cn/acg/?return=json").getData(),
                    Pic::class.java
            ).acgurl
    private var sexUrl:String = GsonBuilder()
            .create().fromJson(
                    FuckOkhttp("https://api.lolicon.app/setu/?apikey=705545485e92e380931b56").getData(),
                    SexImg::class.java
            ).data[0].url
    fun getPic(): String {
        val url = imgurl
        imgurl = ""
        flush()
        return url
    }
    fun getSTPic():String{
        val url = sexUrl
        sexUrl = ""
        flushSex()
        return url
    }
    private fun flushSex(){
        Thread {
            synchronized(sexUrl) {
                sexUrl = GsonBuilder()
                        .create().fromJson(
                                FuckOkhttp("https://api.lolicon.app/setu/?apikey=705545485e92e380931b56").getData(),
                                SexImg::class.java
                        ).data[0].url
            }
        }.start()
    }
    private fun flush(){
        Thread {
            synchronized(imgurl) {
                imgurl = GsonBuilder()
                        .create().fromJson(
                                FuckOkhttp("https://api.blogbig.cn/random/api.php?return=json").getData(),
                                Pic::class.java
                        ).acgurl
            }
        }.start()
    }

}