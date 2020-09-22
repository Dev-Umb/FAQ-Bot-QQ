package Listener

import BotManager.Session
import BotManager.SessionManager
import BotManager.getAnswer
import DB.DB
import DB.DB.database
import DTO.Question
import curd.deleteQuestion
import curd.logger
import curd.quickSearchQuestion
import curd.searchQuestion
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText

class BotMsgListener : BaseListeners() {
    @EventHandler
    suspend fun GroupMessageEvent.onEvent() {
        route(prefix = "", delimiter = " ")  {
            if (SessionManager.performSession(event)) {
                reply("答案录入成功！")
                return@route
            }

            case("#","快速索引"){
                try {
                    val id = event.message
                                .get(PlainText)?.contentToString()?.replace("# ", "")?.toInt()
                    if (id!=null) {
                        val queryRowSet = quickSearchQuestion(id, group)
                        if (queryRowSet!=null) {
                            val tryAnswer = queryRowSet?.let {
                                    getAnswer(it, group)
                            }
                            if (tryAnswer != null) {
                                    reply(tryAnswer)
                            }
                        }
                    }else{
                        throw NumberFormatException("参数错误！请输入问题序号")
                    }
                }catch (e:NumberFormatException){
                    logger().info(e)
                    reply(e.toString())
                }catch (e:NullPointerException){
                    logger().info(e)
                    reply(e.toString())
                }
                return@route
            }

            val tryGetAnswer = searchQuestion(
                    event.message.get(PlainText).toString(), group
                )?.let {
                    getAnswer(it, group)
            }

            if (tryGetAnswer != null) {
                reply(tryGetAnswer)
                return@route
            }

            case("添加问题","添加问题"){
                val question = event.message
                        .get(PlainText)?.contentToString()?.replace("添加问题 ","")
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
                return@route
            }

            case("修改问题","修改一个问题"){
                val question = event.message
                        .get(PlainText)?.contentToString()?.replace("修改问题 ","")

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
                return@route
            }

            case("删除问题","删除一个问题"){
                val question = event.message
                        .get(PlainText)?.contentToString()?.replace("删除问题 ","")
                if (deleteQuestion(question!!,event.group)){
                    reply("已删除问题$question")
                }
                return@route
            }

            case("列表",desc = "获取此群的问题列表"){
                val query =  database
                        .from(Question)
                        .select(Question.question,Question.id)
                        .where {
                    (Question.group eq this.group.id)
                }
                reply(buildString{
                    query.forEach {
                        append("#${it[Question.id]} ${it.get(Question.question)} \n")
                    }
                })
                return@route
            }

            case("帮助","获取帮助指令"){
                reply(getHelp())
                return@route
            }
        }
    }
}