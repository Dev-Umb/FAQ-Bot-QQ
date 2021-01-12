package io.farewell12345.github.faqbot.BotManager

import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.DTO.model.dataclass.Pic
import io.farewell12345.github.faqbot.DTO.model.dataclass.SexImg
import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.sendAsImageTo
import net.mamoe.mirai.utils.ExternalImage
import net.mamoe.mirai.utils.sendTo
import net.mamoe.mirai.utils.toExternalImage
import java.net.URL
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object PicManager {
    val LOLICON = "https://api.lolicon.app/setu/?apikey=705545485e92e380931b56"
    val MAX_PIC_SIZE = 20
    val PicPool = ThreadPoolExecutor(5, 20, 2,
            TimeUnit.HOURS, LinkedBlockingDeque<Runnable>(MAX_PIC_SIZE))
    private var imgurl = GsonBuilder()
            .create().fromJson(
                    FuckOkhttp("https://tenapi.cn/acg/?return=json").getData(),
                    Pic::class.java
            ).acgurl
    private lateinit var sexImgPool: Queue<ExternalImage>
    class PicPoolManage(){
        private fun pushPicToPool(){
            while (true){
                if (sexImgPool.size < MAX_PIC_SIZE){
                    PicPool.execute {
                        val tempPic = URL(
                                GsonBuilder().create().fromJson(
                                        FuckOkhttp(LOLICON).getData(),
                                        SexImg::class.java
                                ).data[0].url
                        ).openConnection().getInputStream().toExternalImage()
                        synchronized(sexImgPool) {
                            sexImgPool.add(tempPic)
                        }
                    }
                }
            }
        }
    }


    fun getPic(): String {
        val url = imgurl
        imgurl = ""
        flush()
        return url
    }

    fun getSTPic(): ExternalImage? {
        return sexImgPool.poll()
    }

//    private fun flushSex() {
//        Thread {
//            synchronized(sexUrl) {
//                sexUrl = GsonBuilder()
//                        .create().fromJson(
//                                FuckOkhttp("https://api.lolicon.app/setu/?apikey=705545485e92e380931b56").getData(),
//                                SexImg::class.java
//                        ).data[0].url
//            }
//        }.start()
//    }

    private fun flush() {
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