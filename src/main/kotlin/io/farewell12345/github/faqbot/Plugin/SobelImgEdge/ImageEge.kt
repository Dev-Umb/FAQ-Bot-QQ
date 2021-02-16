package io.farewell12345.github.faqbot.Plugin.SobelImgEdge

import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import net.mamoe.mirai.utils.MiraiInternalApi
import java.io.*
import java.net.URL
import javax.imageio.ImageIO


object ImageEge {
    @ObsoleteCoroutinesApi
    val threadPool = newFixedThreadPoolContext(10, "ThreadPool")

    @ObsoleteCoroutinesApi
    @MiraiInternalApi
    fun sobelImageEge(event: MessageEvent): Boolean {
        return try {
            val imgList: List<Image> = event.message.filterIsInstance<Image>()
            imgList.forEach {
                GlobalScope.launch(threadPool) {
                    try {
                        val url = it.queryUrl()
                        val con = URL(
                            url
                        ).openConnection()
                        con.connectTimeout = 100
                        val image = con.getInputStream()
                        val bufferImage = ImageIO.read(image)
//                    val imgEge = Sobel().edgeExtract2(file)
                        val bs = ByteArrayOutputStream()
//                    val imOut = ImageIO.createImageOutputStream(bs)
                        ImageIO.write(bufferImage, "png", bs)
                        val inputStream =
                            FuckOkhttp("http://127.0.0.1:8000/img").postFile(bs.toByteArray())?.body?.byteStream()
                        inputStream?.sendAsImageTo(event.subject)
                    } catch (e: Exception) {
                        println(e)
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}