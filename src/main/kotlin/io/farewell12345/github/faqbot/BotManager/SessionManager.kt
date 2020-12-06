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
import kotlin.random.Random

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


object DrawManager{
    private var groupList = mutableMapOf<Long, ArrayList<Int>>()
    fun createDraw(group: Long,star:Int,end:Int):Boolean{
        val tempIntList = ArrayList<Int>()
        if (groupList[group] == null||groupList[group]!!.isEmpty()) {
            for (i in star..end) {
                tempIntList.add(i)
            }
            groupList[group] = tempIntList
            return true
        }
        return false
    }
    fun deleteDraw(group: Long):Boolean{
        groupList.remove(group)
        return true
    }
    fun getInt(group: Long):Int{
        if (groupList[group] == null || groupList[group]!!.isEmpty())
            return -1
        val x = (groupList[group]!!.indices).random()
        val num = groupList[group]?.get(x)!!
        groupList[group]!!.minusAssign(num);
        return num
    }
}

object DisRepetition{
    var groupMap = mutableMapOf<Long,MessageChain>()
    fun thisMessageIsRepetition(msg:GroupMessageEvent):Boolean{
        if (groupMap[msg.group.id]!=null){
            val a = groupMap[msg.group.id]?.get(PlainText).hashCode()+ groupMap[msg.group.id]?.get(Face).hashCode() + groupMap[msg.group.id]?.get(Image).hashCode()+ groupMap[msg.group.id]?.get(At).hashCode()
            val b = msg.message[PlainText].hashCode()+msg.message[Face].hashCode()+msg.message[Image].hashCode()+msg.message[At].hashCode()
            if (a == b){
                groupMap.remove(msg.group.id)
                return true
            }else{
                groupMap[msg.group.id] = msg.message
            }
        }else{
            groupMap[msg.group.id] = msg.message
        }
        return false
    }

}
