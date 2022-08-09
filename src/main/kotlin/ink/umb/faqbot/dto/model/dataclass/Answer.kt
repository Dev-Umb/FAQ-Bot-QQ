package ink.umb.faqbot.dto.model.dataclass

import net.mamoe.mirai.message.data.ForwardMessage
import java.util.*

data class Answer (
        val imgList : LinkedList<String>,
        val atList : LinkedList<Long>,
        val text : String,
        val xmlCardMsg:XmlCardMsg? = null,
        val app:App? = null,
        val forwardMessage: ForwardCard?=null
){
        data class XmlCardMsg(
                var id:Int,
                var content: String
        )
        data class App(
                var content: String
        )
        data class ForwardCard(
                val preview: List<String>,
                val title: String,
                val brief: String,
                val source: String,
                val summary: String,
                val nodeList: List<ForwardMessage.Node>,
        )
}