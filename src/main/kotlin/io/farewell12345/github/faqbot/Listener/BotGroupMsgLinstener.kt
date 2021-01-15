package io.farewell12345.github.faqbot.Listener


import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.SessionManager
import io.farewell12345.github.faqbot.BotManager.*
import io.farewell12345.github.faqbot.DTO.DB.DB
import io.farewell12345.github.faqbot.DTO.DB.DB.database
import io.farewell12345.github.faqbot.DTO.model.*
import io.farewell12345.github.faqbot.DTO.model.dataclass.Answer
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import kotlinx.coroutines.runBlocking
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiInternalApi
import java.net.URL
import java.util.*
import net.mamoe.mirai.message.data.buildMessageChain as buildMessageChain1

class BotGroupMsgListener : BaseListeners() {
    // 重写Event监听事件
    @MiraiInternalApi
    @EventHandler
    suspend fun GroupMessageEvent.onEvent() {
        route(prefix = "", delimiter = " ") {
            if (DisRepetition.thisMessageIsRepetition(event)
                && (group.id in CommandGroupList.DisRepetitionGroupList)) {
                if (event.message.toString() != AppConfig.getInstance().DisRepetitionScence[0]) {
                    subject.sendMessage(AppConfig.getInstance().DisRepetitionScence[0])
                } else {
                    subject.sendMessage(AppConfig.getInstance().DisRepetitionScence[1])
                }
            }
            // 优先进行会话处理
            case("取消", "停止会话录入") {
                if (SessionManager.hasSession(event.sender.id)) {
                    SessionManager.removeQuestion(event.sender.id)
                    SessionManager.removeSession(event.sender.id)
                    subject.sendMessage("取消会话成功！")
                }
            }
            if (!SessionManager.SessionsIsEmpty()) {
                if (SessionManager.performSession(event)) {
                    subject.sendMessage("录入成功！")
                    SessionManager.removeSession(event.sender.id)
                    return@route
                } else if (SessionManager.hasSession(event.sender.id)) {
                    subject.sendMessage("格式有误！答案与问题不能相同，请重新检查录入答案格式或发送‘取消’停止当前对话")
                }
            }
            // 根据问题名称获取回答
            val tryGetAnswer = searchQuestion(
                event.message.firstIsInstanceOrNull<PlainText>().toString(), group.id
            )
                ?.let {
                    getAnswer(it)
                }
            if (tryGetAnswer != null) {
                subject.sendMessage(tryGetAnswer)
                return@route
            }
            furry("#", "快速索引") {
                try {
                    val id = event.message.filterIsInstance<PlainText>()[0].content.replace("#", "").toInt()
                    if (id != null) {
                        val queryRowSet = quickSearchQuestion(id, group)
                        if (queryRowSet != null) {
                            val tryAnswer = getAnswer(queryRowSet)
                            if (tryAnswer != null) {
                                subject.sendMessage(tryAnswer)
                                return@route
                            }
                        } else {
                            subject.sendMessage("此群不存在该序号的问题！")
                        }
                    } else {
                        throw NumberFormatException("参数错误！请输入问题序号")
                    }
                } catch (e: NumberFormatException) {
                    logger.info(e)
                } catch (e: NullPointerException) {
                    logger.info(e)
                } catch (e: Exception) {
                    logger.info(e)
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
                    .replace("创建抽签", "")?.replace(" ", "")
                if (signGroup != null) {
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
                } else {
                    subject.sendMessage("缺少参数！")
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
                if (event.group.id !in CommandGroupList.GameMorningGroupList)
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
                if (group.id in CommandGroupList.AnimationGroupList) {
                    PicManager.stImgSend(subject,event)
                }
                return@route
            }
            case("图来", "二次元图") {
                if (group.id in CommandGroupList.AnimationGroupList) {
                    PicManager.stImgSend(subject,event)
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
            if (event.group.id in CommandGroupList.managerGroupList) {
                if (event.sender.permission.ordinal == 0
                    && event.sender.id != AppConfig.getInstance().SuperUser
                ) {
                    return@route
                }
            }
            case("添加问题", "添加问题",false) {
                val question = event.message
                    .filterIsInstance<PlainText>().firstOrNull()?.
                    content?.replace("添加问题", "")?.replace(" ", "")
                if (question in helpMap.keys) {
                    subject.sendMessage("问题与模块名冲突！,模块名：${helpMap[question]}")
                    return@route
                }
                if (question!!.isEmpty())
                    return@route
                val furry = Regex("""#\d""")
                if (furry.matches(question!!)) {
                    subject.sendMessage("问题不能与索引重名")
                    return@case
                } else {
                    val query = question?.let {
                        searchQuestion(it, event.group.id)
                    }
                    if (query != null) {
                        subject.sendMessage("问题${query[Question.question]}已经存在")
                    } else {
                        DB.database.insert(Question) {
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
            case("修改问题", "修改一个问题",false) {
                var question = event.message
                    .filterIsInstance<PlainText>().firstOrNull()?.toString()?.replace("修改问题", "")
                    ?.replace(" ", "")
                var query = question?.let {
                    searchQuestion(it, event.group.id)
                }
                if (query == null) {
                    try {
                        val id = question?.replace("#", "")?.toInt()
                        query = quickSearchQuestion(id!!, group)
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
            case("删除问题", "删除一个问题",false) {
                var question = event.message
                    .filterIsInstance<PlainText>().firstOrNull()?.content?.
                    replace(" ", "")?.replace("删除问题", "")
                var query = searchQuestion(question!!, group.id)
                if (query == null) {
                    val id = question?.replace("#", "")?.toInt()
                    query = quickSearchQuestion(id!!, group)
                }
                if (query != null) {
                    deleteQuestion(query!!)
                    subject.sendMessage("已删除问题$question")
                } else {
                    subject.sendMessage("没有找到这个问题！")
                }
                return@route
            }
            case("同步问答", "同步不同群的问答消息会将本群问题覆盖",false) {
                val signGroup = event.message
                    .filterIsInstance<PlainText>().firstOrNull()?.content
                try {
                    var groupID = signGroup!!.toLong()
                    val questions = DB.database.from(Question)
                        .select()
                        .where {
                            (Question.group eq groupID)
                        }
                    var QuestionNum = 0;
                    for (i in questions) {
                        if (searchQuestion(
                                question = i[Question.question].toString(),
                                groupID = event.group.id
                            ) == null
                        ) {
                            DB.database.insert(Question) {
                                it.lastEditUser to i[Question.lastEditUser]
                                it.group to event.sender.group.id
                                it.question to i[Question.question]
                                it.answer to i[Question.answer]
                            }
                            ++QuestionNum
                        }
                    }
                    subject.sendMessage("同步成功，共同步$QuestionNum 条问题记录")
                } catch (e: Exception) {
                    subject.sendMessage("请输入有效群号")
                    return@route
                }
            }
        }
    }
}