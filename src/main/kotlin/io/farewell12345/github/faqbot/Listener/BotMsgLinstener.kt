package io.farewell12345.github.faqbot.Listener


import io.farewell12345.github.faqbot.BotManager.Session
import io.farewell12345.github.faqbot.BotManager.SessionManager
import io.farewell12345.github.faqbot.BotManager.*
import io.farewell12345.github.faqbot.DTO.Question
import io.farewell12345.github.faqbot.curd.*
import io.farewell12345.github.faqbot.DB.DB
import io.farewell12345.github.faqbot.DB.DB.database
import io.farewell12345.github.faqbot.BotManager.getAnswer
import io.farewell12345.github.faqbot.curd.deleteQuestion
import io.farewell12345.github.faqbot.curd.quickSearchQuestion
import io.farewell12345.github.faqbot.curd.searchQuestion
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText

class BotMsgListener : BaseListeners() {

    // 重写Event监听事件
    @EventHandler
    suspend fun GroupMessageEvent.onEvent() {

        route(prefix = "", delimiter = " ")  {
            // 优先进行会话处理
            
            if (SessionManager.performSession(event)) {
                reply("答案录入成功！")
                return@route
            }
            furry("#","快速索引"){
                try {
                    val id = event.message
                                .get(PlainText)?.contentToString()?.replace("#", "")?.toInt()
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
                    logger.info(e)
//                    reply("参数错误！请输入问题序号")
                }catch (e:NullPointerException){
                    logger.info(e)
//                    reply("请输入完整序号")
                }catch (e:Exception){
                    logger.info(e)
//                    reply(e.toString())
                }

                return@route
            }

            // 根据问题名称获取回答
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
                    // 新建会话
                    SessionManager.addSession(
                            user = event.sender.id,
                            session = Session(
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
                var query = question?.let {
                    searchQuestion(it, event.group)
                }
                if (query == null){
                    try {
                        val id = question?.replace("#", "")?.toInt()
                        query = quickSearchQuestion(id!!, group)
                    }catch (e:Exception) {
                        reply("问题$question 不存在")
                        return@route
                    }
                }
                if (query!=null){
                    // 新建修改问题会话
                    SessionManager.addSession(
                            user=event.sender.id,
                            session =  Session(
                                    user = event.sender.id,
                                    question = question!!,
                                    type = "upDate",
                                    group = event.group.id
                            )
                    )
                    reply("问题${query[Question.question]}已找到,请问如何修改?")
                    return@route
                }
                reply("问题$question 不存在")
                return@route
            }

            case("删除问题","删除一个问题"){
                var question = event.message
                        .get(PlainText)?.contentToString()?.replace("删除问题 ","")
                if (deleteQuestion(question!!,event.group)){
                    reply("已删除问题$question")
                }else{
                    val id = question.replace("#", "")?.toInt()
                    val query = quickSearchQuestion(id!!, group)
                    if (query!=null)
                        question = query[Question.question]
                        if (deleteQuestion(question!!,event.group)){
                            reply("已删除问题$question")
                        }
                }
                return@route
            }

            case("列表",desc = "获取此群的问题列表"){
                val query =  database
                        .from(Question)
                        .select(Question.question, Question.id)
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

            case("同步问答","同步不同群的问答消息会将本群问题覆盖"){
                val signGroup = event.message
                        .get(PlainText)?.contentToString()?.replace("同步问答 ","")
                try {
                    var groupID = signGroup!!.toLong()
                    val questions = DB.database.from(Question)
                            .select()
                            .where {
                                (Question.group eq groupID)
                            }
                    var QuestionNum = 0;
                    for (i in questions){
                        if (searchQuestion(question= i[Question.question].toString(),group = event.group) ==null) {
                            DB.database.insert(Question) {
                                it.lastEditUser to i[Question.lastEditUser]
                                it.group to event.sender.group.id
                                it.question to i[Question.question]
                                it.answer to i[Question.answer]
                            }
                            ++QuestionNum
                        }
                    }
                    reply("同步成功，共同步$QuestionNum 条问题记录")
                }catch (e:Exception){
                    reply("请输入有效群号")
                    return@route
                }
            }

        }
    }
}