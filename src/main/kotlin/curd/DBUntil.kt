@file:Suppress(
    "EXPERIMENTAL_API_USAGE",
    "DEPRECATION_ERROR",
    "OverridingDeprecatedMember",
    "INVISIBLE_REFERENCE",
    "INVISIBLE_MEMBER"
)
package curd

import BotManager.Session
import DTO.Answer
import DTO.Question
import com.google.gson.Gson
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.OnlineGroupImage
import net.mamoe.mirai.message.data.PlainText
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.StackLocatorUtil
import java.io.File
import java.lang.Exception
import java.net.URL
import java.util.*
fun logger(): Logger {
    return org.apache.logging.log4j.LogManager.getLogger(StackLocatorUtil.getStackTraceElement(2).className)
}

fun deleteQuestion(question: String,group: Group):Boolean{
    try {
        if (searchQuestion(question,group)!=null) {
            DB.DB.database.delete(Question) {
                it.question eq question
            }
            return true
        }
    }catch (e:Exception){
        logger().info(e)
    }
    return false
}


fun searchQuestion(question:String,group: Group): QueryRowSet? {
    val query= DB.DB.database
        .from(Question)
        .select()
        .where {
            (Question.question eq question) and (Question.group eq group.id)
        }
    query.forEach {
        return it
    }
    return null
}

fun downImg(imgUrl:OnlineGroupImage){
    val file = File("img",imgUrl.imageId)
    if (!file.exists()){
        file.createNewFile()
    }
    val output = file.outputStream()
    URL(imgUrl.originUrl).openStream().use {
        it.copyTo(output)
    }
    output.close()
}



fun upDateQuestionAnswer(message: GroupMessageEvent, session: Session): Boolean {
    val imgList = LinkedList<String>()
    val atList = LinkedList<Long>()
    var text = ""
    message.message.forEach {
        when(it){
            is OnlineGroupImage ->{
                downImg(it)
                imgList.add(it.imageId)
            }
            is At ->{
                atList.add(it.target)
            }
            is PlainText ->{
                text =it.content
            }
        }
    }
    try {
        upDate(
            answer = Answer(imgList, atList, text) ,
            session = session
        )
        return true
    }catch (e: Exception){
        logger().info(e)
    }
    return false
}

fun upDate(answer: Answer,session: Session){
    val gson = Gson()
    DB.DB.database.update(Question) {
        it.answer to gson.toJson(answer)
        it.lastEditUser to session.user
        where {
            (Question.question eq session.question) and (Question.group eq session.group)
        }
    }
}