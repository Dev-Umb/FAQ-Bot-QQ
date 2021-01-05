package io.farewell12345.github.faqbot.BotManager

import io.farewell12345.github.faqbot.DTO.model.*
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import net.mamoe.mirai.message.*


object SessionManager{
    private var Sessions= mutableMapOf<Long, Session>()


    fun hasSession(id:Long): Boolean {
        return id in Sessions.keys
    }

    fun removeQuestion(id:Long){
        if (Sessions[id]?.type == "addUpDate"){
            deleteQuestion(searchQuestion(Sessions[id]!!.question, Sessions[id]!!.group)!!)
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

