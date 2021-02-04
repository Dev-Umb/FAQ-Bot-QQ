package io.farewell12345.github.faqbot.BotManager

import io.farewell12345.github.faqbot.DTO.Controller.QuestionController
import io.farewell12345.github.faqbot.DTO.Controller.WelcomeController
import io.farewell12345.github.faqbot.DTO.model.*
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.*
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.MiraiInternalApi


object SessionManager{
    private var Sessions= mutableMapOf<Long, Session>()


    fun hasSession(id:Long): Boolean {
        return id in Sessions.keys
    }

    fun removeQuestion(id:Long){
        if (Sessions[id]?.type == "addUpDate"){
            QuestionController.deleteQuestion(
                QuestionController.searchQuestion(
                    Sessions[id]!!.question,
                    Sessions[id]!!.group)!!
            )
        }
    }

    fun removeSession(id:Long){
        Sessions.remove(id)
    }

    fun SessionsIsEmpty():Boolean{
        return Sessions.isEmpty()
    }

    fun addSession(user:Long, session: Session){
        Sessions.put(user,session)
    }

    @MiraiInternalApi
    fun performSession(messageEvent: GroupMessageEvent): Boolean {
        var flag = false
        val session = Sessions.get(messageEvent.sender.id)
        if (session!=null){
            if(messageEvent.message.filterIsInstance<PlainText>().firstOrNull()?.
                content?.replace(" ","") == session.question){
                return false
            }
            flag=true
            when(session.type){
                "addUpDate" ->
                    return QuestionController.upDateQuestionAnswer(messageEvent,session)
                "changeUpDate"->
                    return QuestionController.upDateQuestionAnswer(messageEvent,session)
                "changeWelcome" ->
                    return WelcomeController.changeWelcome(messageEvent.group,
                        messageEvent.message
                    )
            }

        }
        return flag
    }
}

