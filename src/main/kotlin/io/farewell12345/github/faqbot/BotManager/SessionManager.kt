package io.farewell12345.github.faqbot.BotManager

import io.farewell12345.github.faqbot.DTO.Answer
import io.farewell12345.github.faqbot.DTO.Question
import com.google.gson.Gson
import io.farewell12345.github.faqbot.curd.changeWelcome
import io.farewell12345.github.faqbot.curd.deleteQuestion
import io.farewell12345.github.faqbot.curd.searchQuestion
import io.farewell12345.github.faqbot.curd.upDateQuestionAnswer
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.*
import net.mamoe.mirai.message.data.*
import java.lang.NullPointerException

data class Session(
        val group:Long,
        val user:Long,
        val question:String,
        val type:String
)
object SessionManager{
    private var Sessions= mutableMapOf<Long, Session>()

    fun hasSession(id:Long): Boolean {
        return id in Sessions.keys
    }




    fun removeSesssion(id:Long){
        if (Sessions[id]?.type == "addUpDate"){
            deleteQuestion(searchQuestion(Sessions[id]!!.question, Sessions[id]!!.group)!!)
        }
        Sessions.remove(id)
    }



    fun SessionsIsEmpty():Boolean{
        return Sessions.isEmpty()
    }

    fun addSession(user:Long, session: Session){
        Sessions.put(user,session)
    }

    fun performSession(messageEvent: GroupMessageEvent): Boolean {
        var flag = false
        val session = Sessions.get(messageEvent.sender.id)
        if (session!=null){
            flag=true
            when(session.type){
                "addUpDate" -> return upDateQuestionAnswer(messageEvent,session)
                "changeUpDate"-> return upDateQuestionAnswer(messageEvent,session)
                "changeWelcome" ->return changeWelcome(messageEvent.group,messageEvent.message)
            }

        }
        return flag
    }
}

fun analyticalAnswer(query: QueryRowSet):MessageChain{
    val answerJson = query[Question.answer]
    val gson = Gson()
    val answer =gson.fromJson(answerJson, Answer::class.java)
    val messageChain=MessageChainBuilder()
    messageChain.add(answer.text)
    if (answer.atList.size>0) {
        answer.atList.forEach {
            messageChain.add(At(
                    Bot.botInstances[0]
                        .getGroup(query[Question.group]!!)[it]
                ))
        }
    }
    if (answer.imgList.size>0) {
        answer.imgList.forEach {
            messageChain.add(Image(it))
        }
    }
    return messageChain.build()
}

fun getAnswer(query: QueryRowSet): MessageChain? {
    try {
        return analyticalAnswer(query)
    }catch (e:NullPointerException){
        return null
    }
}