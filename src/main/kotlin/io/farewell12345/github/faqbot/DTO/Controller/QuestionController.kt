package io.farewell12345.github.faqbot.DTO.Controller

import com.google.gson.Gson
import io.farewell12345.github.faqbot.DTO.DB.DB
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question
import io.farewell12345.github.faqbot.DTO.model.dataclass.Answer
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import io.farewell12345.github.faqbot.DTO.model.logger
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiInternalApi
import java.lang.Exception
import java.lang.NullPointerException
import java.util.*

object QuestionController {
    fun analyticalAnswer(query: QueryRowSet): MessageChain {
        val answerJson = query[Question.answer]
        val gson = Gson()
        val answer =gson.fromJson(answerJson, Answer::class.java)
        val messageChain= buildMessageChain {
            +PlainText(answer.text)
            answer.imgList.forEach {
                append(Image(it))
            }
            answer.atList.forEach {
                Bot.instances[0]
                    .getGroup(query[Question.group]!!)?.get(it)?.let { it1 ->
                        append(At(it1))
                    }
            }
        }
        return messageChain
    }
    fun getAnswer(query: QueryRowSet): MessageChain? {
        try {
            return analyticalAnswer(query)
        }catch (e: NullPointerException){
            return null
        }
    }
    fun deleteQuestion(question: QueryRowSet):Boolean{
        try {
            DB.database.delete(Question) {
                it.id eq question[Question.id]!!.toInt()
            }
            return true
        }catch (e: Exception){
            logger().info(e)
        }
        return false
    }

    fun searchQuestion(question:String,groupID: Long): QueryRowSet? {
        try {
            val query = DB.database
                .from(Question)
                .select()
                .where {
                    (Question.question eq question) and (Question.group eq groupID)
                }
            query.forEach {
                return it
            }
        }catch (e: Exception) {

        }
        return null
    }

    fun quickSearchQuestion(id:Int,group: Group): QueryRowSet? {
        val query= DB.database
            .from(Question)
            .select()
            .where {
                (Question.id eq id) and (Question.group eq group.id)
            }
        query.forEach {
            return it
        }
        return null
    }

    @MiraiInternalApi
    fun upDateQuestionAnswer(message: GroupMessageEvent, session: Session): Boolean {
        val imgList = LinkedList<String>()
        val atList = LinkedList<Long>()
        var text = ""
        message.message.forEach {
            when(it){
                is GroupImage ->{
                    //downImg(it)
                    imgList.add(it.imageId)
                }
                is At ->{
                    atList.add(it.target)
                }
                is PlainText ->{
                    text += it.content
                }
            }
        }
        try {
            return upDateAnswer(
                answer = Answer(imgList, atList, text) ,
                session = session
            )
        }catch (e: Exception){
            logger().info(e)
        }
        return false
    }

    fun upDateAnswer(answer: Answer, session: Session):Boolean{
        val gson = Gson()
        val json = gson.toJson(answer)
        if (session.question == answer.text){
            return false
        }
        DB.database.update(Question) {
            it.answer to json
            it.lastEditUser to session.user
            where {
                (Question.question eq session.question) and (Question.group eq session.group)
            }
        }
        return true
    }
}