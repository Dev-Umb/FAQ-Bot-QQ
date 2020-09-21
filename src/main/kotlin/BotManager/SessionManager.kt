package BotManager

import DTO.Answer
import DTO.Question
import com.google.gson.Gson
import curd.searchQuestion
import curd.upDateQuestionAnswer
import io.ktor.util.*
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.message.*
import net.mamoe.mirai.message.data.*
import java.io.File

data class Session(
        val group:Long,
        val user:Long,
        val question:String,
        val type:String
)
object SessionManager{
    private var Sessions= mutableMapOf<Long,Session>()

    fun addSession(user:Long, session: Session){
        Sessions.put(user,session)
    }

    fun performSession(messageEvent: GroupMessageEvent): Boolean {
        var flag = false
        val session = Sessions.get(messageEvent.sender.id)
        if (session!=null){
            flag=true
            when(session.type){
                "upDate"-> upDateQuestionAnswer(messageEvent,session)
            }
            Sessions.remove(messageEvent.sender.id)
        }
        return flag
    }
}

fun analyticalAnswer(query: QueryRowSet):MessageChain{
    val answerJson = query[Question.answer]
    val gson = Gson()
    val answer =gson.fromJson(answerJson,Answer::class.java)
    val messageChain=MessageChainBuilder()
    messageChain.add(answer.text)
    answer.atList.forEach {
        messageChain.add(
                At(
                    Bot.botInstances[0]
                        .getGroup(query[Question.group]!!)[it]
                )
        )
    }
    answer.imgList.forEach {
        messageChain.add(Image(it))
    }
    return messageChain.build()
}

fun getAnswer(question: String,group: Group): MessageChain? {
    val query = searchQuestion(question,group)
    return query?.let {
        analyticalAnswer(it)
    }
}