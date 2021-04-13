package io.farewell12345.github.faqbot.BotManager

import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.DTO.model.dataclass.Pic
import io.farewell12345.github.faqbot.DTO.model.dataclass.SexImg
import io.farewell12345.github.faqbot.DTO.model.logger
import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.FileNotFoundException
import java.net.URL
import java.util.*
import java.util.concurrent.*

object PicManager {
    val LOLICON = "https://api.lolicon.app/setu/?apikey=yourapikey"
    val TENAPI = "https://tenapi.cn/acg/"
    val MAX_PIC_SIZE = 20
    val PicPool = ThreadPoolExecutor(
        5, 20, 4,
        TimeUnit.HOURS, LinkedBlockingDeque(MAX_PIC_SIZE)
    )
    private var sexImgPool: Queue<Image> = LinkedList()
    fun Queue<Image>.isFull(): Boolean = run {
        return this.size >= MAX_PIC_SIZE
    }

    private val PoolManage = PicPoolManage()

    class PicPoolManage() {
        init {
            logger().info("涩图弹药开始填充")
            Thread {
                pushPicPool()
            }.start()
        }


        private fun getSexImgUrl(): String {
            return GsonBuilder().create().fromJson(
                FuckOkhttp(LOLICON).getData(),
                SexImg::class.java
            ).data[0].url
        }


        fun getStImageExternalResource(): ExternalResource {
            return URL(
                getSexImgUrl()
            ).openConnection().getInputStream().toExternalResource()
        }

        private fun pushPicPool() {
            (0..20).forEach {
                try {
                    PicPool.execute {
                        synchronized(sexImgPool) {
                            runBlocking {
                                sexImgPool.add(getStImageExternalResource().uploadAsImage(BotsManager.oneBot!!.asFriend))
                            }
                        }
                    }
                } catch (e: RejectedExecutionException) {

                } catch (e: FileNotFoundException) {

                }
            }
        }
    }

    fun imgSend(subject: Contact, event: MessageEvent) {
        runBlocking {
            subject.sendImage(URL(TENAPI)
                .openConnection().getInputStream())
        }
    }

    private fun getSTPic(contact: Contact): Image? {
        if (sexImgPool.isEmpty()) {
            return null
        }
        PicPool.execute {
            synchronized(sexImgPool) {
                runBlocking {
                    sexImgPool.add(PoolManage.getStImageExternalResource().uploadAsImage(contact))
                    println("弹药装填${sexImgPool.size}")
                }
            }
        }
        return sexImgPool.poll()
    }

    fun stImgSend(subject: Contact, event: MessageEvent) {
        Thread {
            runBlocking {
                val st = getSTPic(subject)
                if (st == null) {
                    subject.sendMessage("弹药装填中...")
                    return@runBlocking
                }
                subject.sendMessage("涩图太涩了，让我先自己康康再私发给你")
                try {
                    if (subject is Friend){
                        subject.sendMessage(st)
                        return@runBlocking
                    }
                    BotsManager.oneBot
                        ?.getFriend(event.sender.id)
                        ?.sendMessage(st)
                }catch (e:NoSuchElementException){
                    val messageChain = buildMessageChain {
                        +PlainText("发送失败，请加我好友")
                        add(At(event.sender))
                    }
                    subject.sendMessage(messageChain)
                } catch (e: Exception) {
                    Thread.sleep(500)
                    subject.sendMessage(st)
                }
            }
        }.start()
    }
}
