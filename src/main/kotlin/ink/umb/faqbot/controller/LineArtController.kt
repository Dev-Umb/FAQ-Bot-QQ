package ink.umb.faqbot.controller

import ink.umb.faqbot.plugin.LineArt.LineArt
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Image

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