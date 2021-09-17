package io.farewell12345.github.faqbot.Plugin.LineArt

import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import io.ktor.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl

object LineArt {
    @OptIn(InternalAPI::class, net.mamoe.mirai.utils.MiraiInternalApi::class)
    fun LineArt(image: Image, messageEvent: MessageEvent): Boolean {
        GlobalScope.launch{
            val img = image.queryUrl()
            val data = FuckOkhttp(img).getBytes()
            val lineArt = FuckOkhttp("http://127.0.0.1:8000/img").postFile(data!!)?.body?.byteStream()
            val msg = buildMessageChain {
                if (lineArt != null) {
                    append(messageEvent.subject.uploadImage(lineArt))
                }
            }
            messageEvent.subject.sendMessage(msg)
        }
        return true
    }
}