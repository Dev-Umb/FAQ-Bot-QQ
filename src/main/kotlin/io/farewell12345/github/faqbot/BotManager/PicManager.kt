package io.farewell12345.github.faqbot.BotManager

import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.DTO.model.dataclass.Pic
import io.farewell12345.github.faqbot.DTO.model.dataclass.SexImg
import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import okhttp3.internal.wait
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.util.*
import java.util.concurrent.*

object PicManager {
    val LOLICON = "https://api.lolicon.app/setu/?apikey=705545485e92e380931b56"
    val TENAPI = "https://tenapi.cn/acg/?return=json"
    val MAX_PIC_SIZE = 20
    val PicPool = ThreadPoolExecutor(5, 20, 4,
            TimeUnit.HOURS, LinkedBlockingDeque(MAX_PIC_SIZE))
    private var sexImgPool: Queue<Image> = LinkedList()
    fun Queue<Image>.isFull(): Boolean = run {
        return this.size >= MAX_PIC_SIZE
    }
    private val PoolManage = PicPoolManage()
    class PicPoolManage(){
        init {
            Thread{
                pushPicPool()
            }.start()
        }
        fun getImgUrl():String{
            return  GsonBuilder().create().fromJson(
                FuckOkhttp(TENAPI).getData(),
                Pic::class.java
            ).acgurl
        }
        fun getImageExternalResource(): ExternalResource {
            return URL(
                getImgUrl()
            ).openConnection().getInputStream().toExternalResource()
        }
        private fun pushPicPool(){
            (0..20).forEach {
                try {
                    PicPool.execute {
                        synchronized(sexImgPool) {
                            runBlocking {

                                sexImgPool.add(getImageExternalResource().uploadAsImage(BotsManager.oneBot!!.asFriend))
                            }
                        }
                    }
                } catch (e: RejectedExecutionException) {

                } catch (e: FileNotFoundException) {

                }
            }
        }
    }

    fun getSTPic(contact: Contact): Image? {
        if (sexImgPool.isEmpty()){
            return null
        }
        PicPool.execute {
            synchronized(sexImgPool) {
                runBlocking {
                    sexImgPool.add(PoolManage.getImageExternalResource().uploadAsImage(contact))
                    println("弹药装填${sexImgPool.size}")
                }
            }
        }
        return sexImgPool.poll()
    }
    fun stImgSend(subject:Contact,event:MessageEvent){
        Thread {
            runBlocking {
                val st = PicManager.getSTPic(subject)
                subject.sendMessage("涩图太涩了，让我先自己康康再给你，久等一下")
                if (st == null) {
                    subject.sendMessage("弹药装填中...")
                    return@runBlocking
                } else {
                    try {
                        subject.sendMessage(st)
                    } catch (e: Exception) {
                        Thread.sleep(500)
                        try {
                            subject.sendMessage(st)
                        } catch (e: NoSuchElementException) {
                            val messageChain = buildMessageChain {
                                +PlainText("发送失败，请加我好友")
                                add(At(event.sender))
                            }
                            subject.sendMessage(messageChain)
                        }
                    }
                }
            }
        }.start()
    }
}
