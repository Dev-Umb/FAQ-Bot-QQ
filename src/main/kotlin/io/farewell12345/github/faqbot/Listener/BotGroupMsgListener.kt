package io.farewell12345.github.faqbot.Listener


import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.*
import io.farewell12345.github.faqbot.DTO.Controller.QuestionController
import io.farewell12345.github.faqbot.DTO.DB.DB.database
import io.farewell12345.github.faqbot.DTO.model.*
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiInternalApi
import java.util.*

class BotGroupMsgListener : BaseListeners() {
    // 重写Event监听事件
    @MiraiInternalApi
    @EventHandler
    suspend fun GroupMessageEvent.onEvent() {
        route(prefix = "", delimiter = " ") {
            if (SessionManager.hasSession(sender.id)) {
                if (SessionManager.performSession(event)) {

                    subject.sendMessage("录入成功！任务正在处理，请稍等")
                    return@route
                }
                subject.sendMessage("格式有误！请检查录入答案格式")
            }

            case("取消", "停止会话录入") {
                if (SessionManager.hasSession(event.sender.id)) {
                    removeQ(event.sender.id)
                    subject.sendMessage("取消会话成功！")
                }
            }
            case("列表", desc = "获取此群的问题列表") {
                val query = database
                    .from(Question)
                    .select()
                    .where {
                        (Question.group eq event.group.id)
                    }
                subject.sendMessage(buildString {
                    query.forEach {
                        append("#${it[Question.id]} ${it.get(Question.question)} \n")
                    }
                })
                return@route
            }
            val tryGetAnswer = QuestionController.searchQuestion(
                event.message.firstIsInstanceOrNull<PlainText>().toString(), group.id
            )?.let {
                QuestionController.getAnswer(it)
            }
            if (tryGetAnswer != null) {
                subject.sendMessage(tryGetAnswer)
                return@route
            }
            furry("#", "快速索引") {
                try {
                    val id = event.message.filterIsInstance<PlainText>()[0].content.replace("#", "").toInt()
                    val queryRowSet = QuestionController.quickSearchQuestion(id, group)
                    if (queryRowSet != null) {
                        val tryAnswer = QuestionController.getAnswer(queryRowSet)
                        if (tryAnswer != null) {
                            subject.sendMessage(tryAnswer)
                            return@route
                        }
                    } else {
                        subject.sendMessage("此群不存在该序号的问题！")
                    }
                } catch (e: NumberFormatException) {
                    logger.info(e)
                } catch (e: NullPointerException) {
                    logger.info(e)
                } catch (e: Exception) {
                    logger.info(e)
                }
            }
            if (event.group.id in CommandGroupList.managerGroupList) {
                if (event.sender.permission.ordinal == 0
                    && event.sender.id != AppConfig.getInstance().superUser
                ) {
                    return@route
                }
            }
            case("添加问题", "添加问题", false) {
                val question = commandText
                if (question in helpMap.keys) {
                    subject.sendMessage("问题与模块名冲突！,模块名：${helpMap[question]}")
                    return@route
                }
                if (question!!.isEmpty())
                    return@route
                val furry = Regex("""#\d""")
                if (furry.matches(question)) {
                    subject.sendMessage("问题不能与索引重名")
                    return@case
                } else {
                    val query = question.let {
                        QuestionController.searchQuestion(it, event.group.id)
                    }
                    if (query != null) {
                        subject.sendMessage("问题${query[Question.question]}已经存在")
                    } else {
                        database.insert(Question) {
                            set(it.lastEditUser, event.sender.id)
                            set(it.group, event.sender.group.id)
                            set(it.question, question)
                        }
                        subject.sendMessage("问题${question}已被录入,请问如何回答?")
                        // 新建会话
                        SessionManager.addSession(
                            user = event.sender.id,
                            session = Session(
                                user = event.sender.id,
                                question = question,
                                type = "addUpDate",
                                group = event.group.id
                            )
                        )
                    }
                }
                return@route
            }
            case("修改问题", "修改一个问题", false) {
                var question = commandText
                var query = question.let {
                    QuestionController.searchQuestion(it, event.group.id)
                }
                if (query == null) {
                    try {
                        val id = question.replace("#", "").toInt()
                        query = QuestionController.quickSearchQuestion(id, group)
                        question = query?.get(Question.question).toString()
                    } catch (e: Exception) {
                        subject.sendMessage("问题$question 不存在")
                        return@route
                    }
                }
                if (query != null) {
                    // 新建修改问题会话
                    SessionManager.addSession(
                        user = event.sender.id,
                        session = Session(
                            user = event.sender.id,
                            question = question,
                            type = "changeUpDate",
                            group = event.group.id
                        )
                    )
                    subject.sendMessage("问题${query[Question.question]}已找到,请问如何修改?")
                    return@route
                }
                subject.sendMessage("问题$question 不存在")
                return@route
            }
            case("删除问题", "删除一个问题", false) {
                val question = commandText
                var query = QuestionController.searchQuestion(question!!, group.id)
                if (query == null) {
                    val id = question.replace("#", "")?.toInt()
                    query = QuestionController.quickSearchQuestion(id!!, group)
                }
                if (query != null) {
                    QuestionController.deleteQuestion(query!!)
                    subject.sendMessage("已删除问题$question")
                } else {
                    subject.sendMessage("没有找到这个问题！")
                }
                return@route
            }
            case("同步问答", "同步不同群的问答消息会将本群问题覆盖", false) {
                val signGroup = commandText
                try {
                    val groupID = signGroup!!.toLong()
                    val questions = database.from(Question)
                        .select()
                        .where {
                            (Question.group eq groupID)
                        }
                    var questionNum = 0
                    for (i in questions) {
                        if (QuestionController.searchQuestion(
                                question = i[Question.question].toString(),
                                groupID = event.group.id
                            ) == null
                        ) {
                            database.insert(Question) {
                                it.lastEditUser to i[Question.lastEditUser]
                                it.group to event.sender.group.id
                                it.question to i[Question.question]
                                it.answer to i[Question.answer]
                            }
                            ++questionNum
                        }
                    }
                    subject.sendMessage("同步成功，共同步$questionNum 条问题记录")
                } catch (e: Exception) {
                    subject.sendMessage("请输入有效群号${e.message}")
                    return@route
                }
            }
            case("帮助", "获取帮助指令") {
                subject.sendMessage(getHelp())
                return@route
            }
        }
    }
}