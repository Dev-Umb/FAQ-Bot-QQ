package ink.umb.faqbot.listener

import ink.umb.faqbot.AppConfig
import ink.umb.faqbot.bot.manager.CommandGroupList
import ink.umb.faqbot.bot.manager.SessionManager
import ink.umb.faqbot.bot.manager.removeSession
import ink.umb.faqbot.controller.QuestionController
import ink.umb.faqbot.dto.db.DB.database
import ink.umb.faqbot.dto.model.bind.MessageBind
import ink.umb.faqbot.dto.model.bind.QuestionBind
import ink.umb.faqbot.dto.model.Question
import ink.umb.faqbot.dto.model.Question.message
import ink.umb.faqbot.dto.model.Question.question
import ink.umb.faqbot.dto.model.dataclass.Session
import ink.umb.faqbot.plugin.Lucky.Lucky
import ink.umb.faqbot.route.IMessageEvent
import ink.umb.faqbot.route.route
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.entity.update
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import net.mamoe.mirai.utils.MiraiInternalApi

class BotGroupMsgListener : BaseListeners(), IMessageEvent {
    // 重写Event监听事件
    @OptIn(MiraiInternalApi::class)
    @EventHandler
    override suspend fun GroupMessageEvent.onEvent() {
        route(prefix = "", delimiter = " ") {
            case("取消", "停止会话录入") {
                if (SessionManager.hasSession(event.sender.id)) {
                    removeSession(event.sender.id)
                    subject.sendMessage("取消会话成功！")
                }
                return@route
            }
            if (SessionManager.hasSession(sender.id)) {
                if (SessionManager.performSession(event)) {
                    subject.sendMessage("录入成功！任务正在处理，请稍等")
                    return@route
                }
                subject.sendMessage("格式有误！请检查录入答案格式")
            }

            case("列表", desc = "获取此群的问题列表") {
                val query = database.question.filter{
                    it.group eq group.id
                }.toList()
                subject.sendMessage(buildString {
                    query.forEach {q ->
                        append("#${q.id} ${
                            q.question.data
                        } \n")
                    }
                })
                return@route
            }
            case("求签","求签"){
                if(CommandGroupList.luckyGroup[group.id] == true)
                    subject.sendMessage(Lucky.getDraw(sender,message))
            }
            case("线稿","线稿转换"){
                SessionManager.addSession(
                    user = event.sender.id,
                    session = Session(
                        user = event.sender.id,
                        type = "LineArt",
                        group = event.group
                    )
                )
                subject.sendMessage("请发送你的图片")
            }
            val tryGetAnswer = QuestionController.searchQuestion(
                event.message.firstIsInstanceOrNull<PlainText>().toString(), group
            )?.let {
                QuestionController.getAnswer(it,group.id)
            }
            if (tryGetAnswer != null) {
                subject.sendMessage(tryGetAnswer)
                return@route
            }
            furry("#", "快速索引") {
                try {
                    val id = event.message.filterIsInstance<PlainText>()[0]
                        .content.replace("#", "").trim().toInt()
                    val answer: QuestionBind = QuestionController.quickSearchQuestion(id, group)?: throw Exception("此群不存在该序号的问题！")
                    val tryAnswer = QuestionController.getAnswer(answer,group.id)
                    if (tryAnswer != null) {
                            subject.sendMessage(tryAnswer)
                            return@route
                    }
                } catch (_: Exception) {

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
                if (question.isEmpty())
                    return@route
                val furry = Regex("""#\d""")
                if (furry.matches(question)) {
                    subject.sendMessage("问题不能与索引重名")
                    return@case
                } else {
                    val questionResult = question.let {
                        QuestionController.searchQuestion(it, event.group)
                    }
                    if (questionResult!= null) {
                        subject.sendMessage("问题${questionResult.question.data}已经存在")
                    } else {
                        val message = MessageBind{
                            this.data = question
                        }
                        database.message.add(message)
                        val q = QuestionBind{
                            this.question = message
                            this.lastEditUser = event.sender.id.toString()
                            this.group = event.sender.group.id
                        }
                        database.question.add(q)
                        message.questionId = q.id
                        database.message.update(message)
                        subject.sendMessage("问题${question}已被录入,请问如何回答?")
                        // 新建会话
                        SessionManager.addSession(
                            user = event.sender.id,
                            session = Session(
                                user = event.sender.id,
                                data = question,
                                type = "addUpDate",
                                group = event.group
                            )
                        )
                    }
                }
                return@route
            }
            case("修改问题", "修改一个问题", false) {
                var question = commandText
                var result = question.let {
                    QuestionController.searchQuestion(it, event.group)
                }
                if (result == null) {
                    try {
                        val id = question.replace("#", "").toInt()
                        result = QuestionController.quickSearchQuestion(id, group)
                        question = result!!.question.data
                    } catch (e: Exception) {
                        subject.sendMessage("问题$question 不存在")
                        return@route
                    }
                }
                // 新建修改问题会话
                SessionManager.addSession(
                    user = event.sender.id,
                    session = Session(
                        user = event.sender.id,
                        data = question,
                        type = "changeUpDate",
                        group = event.group
                    )
                )
                subject.sendMessage("问题${question}已找到,请问如何修改?")
                return@route
            }
            case("删除问题", "删除一个问题", false) {
                val question = commandText
                var result = QuestionController.searchQuestion(question, group)
                if (result == null) {
                    val id = question.replace("#", "").toInt()
                    result = QuestionController.quickSearchQuestion(id, group)
                }
                if (result != null) {
                    QuestionController.deleteQuestion(result!!)
                    subject.sendMessage("已删除问题$question")
                } else {
                    subject.sendMessage("没有找到这个问题！")
                }
                return@route
            }
            case("同步问答", "同步不同群的问答消息会将本群问题覆盖", false) {
                val signGroup = commandText
                try {
                    val groupID = signGroup.toLong()
                    val questions = database.question.filter {
                            (Question.group eq groupID)
                        }.toList()
                    var questionNum = 0
                    for (i in questions) {
                        if (QuestionController.searchQuestion(
                                question = i.question.data,
                                groupID = event.group
                            ) == null
                        ) {
                            val Q = MessageBind{
                                data = i.question.data
                            }
                            val A =  MessageBind{
                                data = i.answer.data
                            }
                            database.message.add(Q)
                            database.message.add(A)
                            val question = QuestionBind{
                                lastEditUser = i.lastEditUser
                                group = event.sender.group.id
                                question = Q
                                answer = A
                            }
                            database.question.add(question)
                            Q.questionId=question.id
                            A.questionId=question.id
                            database.message.update(Q)
                            database.message.update(A)
                            ++questionNum
                        }
                    }
                    subject.sendMessage("同步成功，共同步$questionNum 条问题记录")
                } catch (e: Exception) {
                    subject.sendMessage("请输入有效群号${e.message}")
                    return@route
                }
            }
            case("添加定时任务","添加定时任务"){
                SessionManager.addSession(
                    user = event.sender.id,
                    session = Session(
                        user = event.sender.id,
                        type = "timerTask",
                        group = event.group
                    )
                )
                subject.sendMessage("请输入定时消息")
            }
            case("帮助", "获取帮助指令") {
                subject.sendMessage(getHelp())
                return@route
            }

        }
    }
}