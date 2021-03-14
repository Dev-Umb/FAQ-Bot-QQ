package io.farewell12345.github.faqbot.Listener


import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.SessionManager
import io.farewell12345.github.faqbot.BotManager.*
import io.farewell12345.github.faqbot.DTO.Controller.GameController
import io.farewell12345.github.faqbot.DTO.Controller.QuestionController
import io.farewell12345.github.faqbot.DTO.DB.DB
import io.farewell12345.github.faqbot.DTO.DB.DB.database
import io.farewell12345.github.faqbot.DTO.model.*
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Games.Game
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Games.User
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import io.farewell12345.github.faqbot.Plugin.Lucky.Lucky
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
            if (DisRepetition.thisMessageIsRepetition(event)
                && (group.id in CommandGroupList.disRepetitionGroupList)
            ) {
                if (event.message.firstIsInstanceOrNull<PlainText>()!!.content != AppConfig.getInstance().disRepetitionScence[0]) {
                    subject.sendMessage(AppConfig.getInstance().disRepetitionScence[0])
                } else {
                    subject.sendMessage(AppConfig.getInstance().disRepetitionScence[1])
                }
            }
            if (SessionManager.hasSession(sender.id)) {
                if (SessionManager.performSession(event)) {

                    subject.sendMessage("录入成功！任务正在处理，请稍等")
                    return@route
                }
                subject.sendMessage("格式有误！请检查录入答案格式")
            }
            // 优先进行会话处理
            case("禁止转发") {
                CommandGroupList.forwardMessageGroup[group.id] = false
                subject.sendMessage("好")
                return@route
            }
            case("线稿", "图片转线稿") {
                if (SessionManager.hasSession(sender.id)) {
                    subject.sendMessage("你有正在进行的会话")
                    return@route
                }
                SessionManager.addSession(
                    user = sender.id,
                    session = Session(
                        user = sender.id,
                        type = "Sobel",
                        group = group.id
                    )
                )
                subject.sendMessage("请发送你的图片")
            }
            case("可以转发") {
                CommandGroupList.forwardMessageGroup[group.id] = true
                subject.sendMessage("好")
                return@route
            }
            case("加入活动", "将该用户加入目标活动，如果没有活动，则创建新的活动") {
                val activity = this.message.filterIsInstance<PlainText>().firstOrNull()?.content?.replace("加入活动 ", "")
                if (activity?.length!! < 2) return@route
                if (GameController.addMemberToGame(activity, group.id, sender.id)) {
                    subject.sendMessage("已加入活动$activity")
                } else {
                    subject.sendMessage("你已在活动$activity 中")
                }
            }
            case("退出活动") {
                val activity = this.message.filterIsInstance<PlainText>().firstOrNull()?.content?.replace("退出活动 ", "")
                if (activity?.length!! < 2) return@route
                if (GameController.rollbackGame(activity, sender.id, group.id)) {
                    subject.sendMessage("退出成功")
                } else {
                    subject.sendMessage("退出失败，你没有加入这个活动或该活动不存在")
                }
            }
            case("活动列表") {
                var activityList = buildString {
                    GameController.getGroupGamesRowSet(group.id).query.forEach {
                        append(it[Game.name])
                        append("\n")
                    }
                }
                if (activityList.length < 2) {
                    activityList = "此群暂无活动"
                }
                subject.sendMessage(activityList)
            }
            case("来", "召唤活动参与者们") {
                val activity: String = message.filterIsInstance<PlainText>()
                    .firstOrNull()?.content?.replace("来 ", "") ?: return@route
                val members = buildMessageChain {
                    GameController.getGameMember(activity, group.id).query.forEach {
                        +At(it[User.qq] as Long)
                    }
                }
                if (members.size < 1) return@route
                subject.sendMessage(members)
            }
            case("删除活动") {
                val activity = this.message.filterIsInstance<PlainText>().firstOrNull()?.content?.replace("删除活动 ", "")
                    ?: return@route
                if (!GameController.deleteGame(activity, group.id)) {
                    subject.sendMessage("不存在此活动")
                } else {
                    subject.sendMessage("删除成功")
                }
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
            case("删除抽签", "删除抽签") {
                DrawManager.deleteDraw(group.id)
                subject.sendMessage("删除成功！")
                return@route
            }
            case("创建抽签", "创建抽签") {
                val signGroup = event.message.filterIsInstance<PlainText>()[0].content
                    .replace("创建抽签", "").replace(" ", "")
                val numList = signGroup.split(",")
                if (numList.size == 2) {
                    val star = numList[0].toInt()
                    val end = numList[1].toInt()
                    if (DrawManager.createDraw(group.id, star, end)) {
                        subject.sendMessage("创建成功！")
                        return@route
                    } else {
                        subject.sendMessage("创建失败，或已有抽签活动在进行中")
                        return@route
                    }
                } else {
                    subject.sendMessage("只能包括开始和结束两个参数！")
                }
                return@route
            }
            case("抽签", "群内抽签设置") {
                val x = DrawManager.getInt(group.id)
                if (x == -1) {
                    subject.sendMessage("暂无抽签活动或本次抽签已经结束！")
                    return@route
                }
                subject.sendMessage(At(event.sender).plus(x.toString()))
            }
            case("游戏推荐", "游戏推荐") {
                if (event.group.id !in CommandGroupList.gameMorningGroupList)
                    return@route
                val GameIndex = GameManage.getGame()
                subject.sendMessage(buildString {
                    append(
                        "<%s>\n现价：%s\n评分：%s\n平台：%s\n点评：%s".format(
                            GameIndex.game.name,
                            GameIndex.game.price.current,
                            GameIndex.game.score,
                            GameIndex.game.gameType,
                            GameIndex.description
                        )
                    )
                })
                return@route
            }
            case("涩图来", "ST") {
                if (group.id in CommandGroupList.animationGroupList) {
                    PicManager.stImgSend(subject, event)
                }
                return@route
            }
            case("图来", "二次元图") {
                if (group.id in CommandGroupList.animationGroupList) {
                    PicManager.imgSend(subject, event)
                    return@route
                }
            }
            case("普法", "法律") {
                subject.sendMessage(FuckOkhttp("http://holk.tech:8886").getData())
            }
            case("帮助", "获取帮助指令") {
                subject.sendMessage(getHelp())
                return@route
            }
            case("求签") {
                var things:String? = message.findIsInstance<PlainText>()?.content?.replaceFirst("求签","")?.replace(" ","")
                if (things?.isEmpty() == true) things = null
                val degree = Lucky.getDraw(sender.id,things)
                event.subject.sendMessage(buildMessageChain {
                    append(At(sender))
                    append(
                        "今天是${CommandGroupList.calendar.get(Calendar.MONTH) + 1}月" +
                                "${CommandGroupList.calendar.get(Calendar.DAY_OF_MONTH)}日," +
                                "你所求 ${things ?: "今日整体运势"} 签的结果为" +
                                "${degree}"
                    )
                })
            }
            // 根据问题名称获取回答
            val tryGetAnswer = QuestionController.searchQuestion(
                event.message.firstIsInstanceOrNull<PlainText>().toString(), group.id
            )
                ?.let {
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
                val question = event.message
                    .filterIsInstance<PlainText>().firstOrNull()?.content?.replace("添加问题", "")?.replace(" ", "")
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
                var question = event.message
                    .filterIsInstance<PlainText>().firstOrNull()?.toString()?.replace("修改问题", "")
                    ?.replace(" ", "")
                var query = question?.let {
                    QuestionController.searchQuestion(it, event.group.id)
                }
                if (query == null) {
                    try {
                        val id = question?.replace("#", "")?.toInt()
                        query = QuestionController.quickSearchQuestion(id!!, group)
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
                            question = question!!,
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
                val question = event.message
                    .filterIsInstance<PlainText>().firstOrNull()?.content?.replace(" ", "")?.replace("删除问题", "")
                var query = QuestionController.searchQuestion(question!!, group.id)
                if (query == null) {
                    val id = question?.replace("#", "")?.toInt()
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
                val signGroup = event.message
                    .filterIsInstance<PlainText>().firstOrNull()?.content
                try {
                    val groupID = signGroup!!.toLong()
                    val questions = DB.database.from(Question)
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
                    subject.sendMessage("请输入有效群号")
                    return@route
                }
            }
        }
    }
}