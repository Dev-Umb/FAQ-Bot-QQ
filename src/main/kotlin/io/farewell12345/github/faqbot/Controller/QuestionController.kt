package io.farewell12345.github.faqbot.Controller

import com.google.gson.Gson
import io.farewell12345.github.faqbot.DTO.DB.DB
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Bind.MessageBind
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Bind.QuestionBind
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question.message
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question.question
import io.farewell12345.github.faqbot.DTO.model.dataclass.Answer
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import io.farewell12345.github.faqbot.DTO.model.logger
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import java.util.*

object QuestionController {

    @OptIn(MiraiExperimentalApi::class)
    private fun analyticalAnswer(answerJson: MessageBind, groupId:Long): MessageChain {
//        val answerJson = query[Question.answer]
        val gson = Gson()
        val answer:Answer =gson.fromJson(answerJson.data, Answer::class.java)
        val messageChain= buildMessageChain {
            +PlainText(answer.text)
            answer.imgList.forEach {
                append(Image(it))
            }
            answer.atList.forEach {
                Bot.instances[0]
                    .getGroup(groupId)?.get(it)?.let { it1 ->
                        append(At(it1))
                    }
            }
            if(answer.xmlCardMsg!=null)
                append(SimpleServiceMessage(serviceId = answer.xmlCardMsg.id,content = answer.xmlCardMsg.content))
            if (answer.app!=null){
                append(LightApp(content = answer.app.content))
            }
            if (answer.forwardMessage!=null){
                append(ForwardMessage(preview=answer.forwardMessage.preview,
                    title=answer.forwardMessage.title,
                    brief=answer.forwardMessage.brief,
                    source=answer.forwardMessage.source,
                    summary=answer.forwardMessage.summary,
                    nodeList=answer.forwardMessage.nodeList,))
            }
        }
        return messageChain
    }
    private fun upDateAnswer(answer: Answer, session: Session): Boolean {
        if (session.data == answer.text){
            return false
        }
        val questionUpdate = DB.database.message.filter {
            it.data eq session.data
        }.toList()?:return false
        run {
            questionUpdate.forEach { Q->
                val question = DB.database.question.filter {
                    (it.group eq session.group.id)
                }.filter{
                    (it.id eq Q.questionId)
                }.toList().firstOrNull()
                if (question!=null){
                    val gson = Gson()
                    val json = gson.toJson(answer)
                    question.lastEditUser = session.user.toString()
                    question.flushChanges()
                    var answerUpdate: MessageBind? = null
                    if (question.answer.data.isEmpty()) {
                        answerUpdate = MessageBind{
                            data=json
                            questionId= question.id
                        }
                        question.answer = answerUpdate
                        DB.database.message.add(answerUpdate)
                        DB.database.question.update(question)
                    }else{
                        answerUpdate = question.answer
                        answerUpdate.data = json
                        DB.database.message.update(answerUpdate)
                    }
                    return true
                }
            }
        }
        return false
    }
    fun getAnswer(question: QuestionBind,groupId: Long): MessageChain? {
        try {
            val message = question.answer
            return analyticalAnswer(message,groupId)
        }catch (e: NullPointerException){
            return null
        }
    }
    fun getAnswer(message: MessageBind,groupId: Long): MessageChain? {
        try {
            return analyticalAnswer(message,groupId)
        }catch (e: NullPointerException){
            return null
        }
    }
    fun deleteQuestion(question:QuestionBind):Boolean{
        try {
            DB.database.question.find {
                it.id eq question.id
            }
            val answer = question.answer
            val q = question.question
            question.delete()
            answer.delete()
            q.delete()
            return true
        }catch (e: Exception){
            logger().info(e)
        }
        return false
    }
    fun searchQuestion(question:String, groupID: Group): QuestionBind? {
        return try {
            var answer: QuestionBind? = null
            kotlin.run {
                val allQuestion = DB.database.message.filter { it.data eq question }.toList()
                allQuestion.forEach { Q ->
                    val filerAnswer = DB.database.question.filter {
                        it.id eq Q.questionId
                    }
                    answer = filerAnswer.filter {
                        it.group eq groupID.id
                    }.toList().firstOrNull()
                    if (answer != null) {
                        return@run
                    }
                }
            }
            if (answer==null) throw Exception("null")
            answer
        }catch (e: Exception) {
            null
        }
    }
    fun quickSearchQuestion(id:Int,group: Group): QuestionBind? {
        return DB.database
            .question
            .filter{
                (Question.id eq id)}.filter{
                (Question.group eq group.id)
            }.toList()[0]?:null
    }
    @OptIn(MiraiExperimentalApi::class)
    @MiraiInternalApi
    fun upDateQuestionAnswer(message: GroupMessageEvent, session: Session): Boolean {
        val imgList = LinkedList<String>()
        val atList = LinkedList<Long>()
        var text = ""
        var card: Answer.XmlCardMsg?=null
        var app:Answer.App?=null
        var forwardMessage:Answer.ForwardCard?=null
        message.message.forEach {
            when(it){
                is Image ->{
                    //downImg(it)
                    imgList.add(it.imageId)
                }
                is At ->{
                    atList.add(it.target)
                }
                is PlainText ->{
                    text += it.content
                }
                is SimpleServiceMessage->{
                    card = Answer.XmlCardMsg(id = it.serviceId,content = it.contentToString())
                }
                is LightApp->{
                    app =Answer.App( it.contentToString())
                }
                is ForwardMessage->{

                    forwardMessage= Answer.ForwardCard(
                        preview= it.preview,title = it.title,brief = it.brief,nodeList = it.nodeList,
                        summary = it.summary,source = it.source
                    )
                }
            }
        }
        try {
            return upDateAnswer(
                answer = Answer(imgList, atList, text,card,app,forwardMessage) ,
                session = session
            )
        }catch (e: Exception){
            logger().info(e)
        }
        return false
    }

}