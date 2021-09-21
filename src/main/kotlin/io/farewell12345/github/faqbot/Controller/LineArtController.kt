package io.farewell12345.github.faqbot.Controller

import io.farewell12345.github.faqbot.Plugin.LineArt.LineArt
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.findIsInstance
import net.mamoe.mirai.message.data.toMessageChain

object LineArtController {
    fun getLineArtImg(message: MessageEvent):Boolean{
        message.message.forEach {
            if(it is Image){
                LineArt.LineArt(it,message)
            }
        }
        return true
    }
}