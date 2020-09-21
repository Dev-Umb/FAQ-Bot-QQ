@file:Suppress(
        "EXPERIMENTAL_API_USAGE",
        "DEPRECATION_ERROR",
        "OverridingDeprecatedMember",
        "INVISIBLE_REFERENCE",
        "INVISIBLE_MEMBER"
)


import BotManager.*
import DTO.Question
import DB.DB
import curd.deleteQuestion
import curd.logger
import curd.searchQuestion
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.StackLocatorUtil





suspend fun main() {
    val bot = getBot()
    bot.subscribeAlways<GroupMessageEvent>() { event ->
            val tryGetAnswer= getAnswer(event.message.get(PlainText).toString(),group)
            if (tryGetAnswer!=null){
                reply(tryGetAnswer)
            }

            if(BotManager.SessionManager.performSession(event)){
                reply("答案录入成功！")
            }

            if (event.message.content.contains("列表")){

            }
            if (event.message.content.contains("删除问题 ")){
                val question = event.message.get(PlainText)?.contentToString()
                    ?.replace("删除问题 ","")
                if (deleteQuestion(question!!,event.group)){
                    reply("已删除问题$question")
                }
            }

            if (event.message.content.contains("修改问题 ")){
                val question = event.message.get(PlainText)?.contentToString()
                    ?.replace("修改问题 ","")

                val query = question?.let {
                    searchQuestion(it, event.group)
                }

                if (query == null){
                    reply("问题$question 不存在")
                }else{
                    SessionManager.addSession(
                        user=event.sender.id,
                        session =  Session(
                            user = event.sender.id,
                            question = question,
                            type = "upDate",
                            group = event.group.id
                        )
                    )
                    reply("问题${question}已找到,请问如何修改?")
                }
            }

            if (event.message.content.contains("添加问题 ")) {

                val question = event.message.get(PlainText)?.contentToString()
                    ?.replace("添加问题 ","")

                val query = question?.let {
                    searchQuestion(it, event.group)
                }
                if (query != null) {
                    reply("问题${query[Question.question]}已经存在")
                } else {
                    DB.database.insert(Question) {
                        it.lastEditUser to event.sender.id
                        it.group to event.sender.group.id
                        it.question to question
                    }
                    SessionManager.addSession(
                            user=event.sender.id,
                            session =  Session(
                                    user = event.sender.id,
                                    question = question!!,
                                    type = "upDate",
                                    group = event.group.id
                            )
                    )
                    reply("问题${question}已被录入,请问如何回答?")
                }
            }
        }
    val log = logger()
    bot.join()
}