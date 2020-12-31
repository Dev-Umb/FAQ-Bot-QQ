package io.farewell12345.github.faqbot.Listener


import FuckOkhttp.FuckOkhttp
import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.Session
import io.farewell12345.github.faqbot.BotManager.SessionManager
import io.farewell12345.github.faqbot.BotManager.*
import io.farewell12345.github.faqbot.curd.*
import io.farewell12345.github.faqbot.DB.DB
import io.farewell12345.github.faqbot.DB.DB.database
import io.farewell12345.github.faqbot.BotManager.getAnswer
import io.farewell12345.github.faqbot.DTO.*
import io.farewell12345.github.faqbot.curd.deleteQuestion
import io.farewell12345.github.faqbot.curd.quickSearchQuestion
import io.farewell12345.github.faqbot.curd.searchQuestion
import kotlinx.coroutines.runBlocking
import me.liuwj.ktorm.dsl.*
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import java.io.File
import java.net.URL
import java.util.*

class BotMsgListener : BaseListeners() {

    // 重写Event监听事件
    @EventHandler
    suspend fun GroupMessageEvent.onEvent() {

        route (prefix = ".command",delimiter = " "){

            if (event.sender.permission.ordinal==0
                    && event.sender.id !=AppConfig.getInstance().SuperUser){
                return@route
            }
            case("addGame","开启游戏推荐"){
                if (event.group.id !in CommandGroupList.GameMorningGroupList){
                    CommandGroupList.GameMorningGroupList.add(event.group.id)
                }
                reply("本群游戏推荐已开启")
                return@route
            }
            case("closeGame","关闭游戏推荐"){
                if (event.group.id in CommandGroupList.GameMorningGroupList){
                    CommandGroupList.GameMorningGroupList.remove(event.group.id)
                }
                reply("本群游戏推荐已关闭")
                return@route
            }
            case("Dis","开启反复读"){
                if (event.group.id !in CommandGroupList.DisRepetitionGroupList){
                    CommandGroupList.DisRepetitionGroupList.add(event.group.id)
                }
                reply("反复读已开启")
                return@route
            }
            case("closeDis","关闭反复读"){
                if (event.group.id in CommandGroupList.DisRepetitionGroupList){
                    CommandGroupList.DisRepetitionGroupList.remove(event.group.id)
                }
                reply("反复读已关闭")
                return@route
            }
            case("Animation","开启发图"){
                if (event.group.id !in CommandGroupList.AnimationGroupList){
                    CommandGroupList.AnimationGroupList.add(event.group.id)
                }
                reply("图片发送已开启")
                return@route
            }
            case("closeAnim","关闭反复读"){
                if (event.group.id in CommandGroupList.AnimationGroupList){
                    CommandGroupList.AnimationGroupList.remove(event.group.id)
                }
                reply("图片发送已关闭")
                return@route
            }
            case("closeWel","关闭迎新"){
                if (event.group.id in CommandGroupList.welcomeGroupList){
                    CommandGroupList.welcomeGroupList.remove(event.group.id)
                }
                reply("本群迎新功能已关闭")
                return@route
            }
            case("manage","仅限管理员进行操作"){
                if (event.group.id !in CommandGroupList.welcomeGroupList){
                    CommandGroupList.managerGroupList.add(event.group.id)
                }
                reply("开启词条管理！")
                return@route
            }
            case("dismanage","关闭仅限管理员进行操作"){
                if (event.group.id in CommandGroupList.welcomeGroupList){
                    CommandGroupList.managerGroupList.remove(event.group.id)
                }
                reply("关闭词条管理！")
                return@route
            }
            case("welcome","开启迎新词"){
                if (event.group.id !in CommandGroupList.welcomeGroupList) {
                    CommandGroupList.welcomeGroupList.add(this.group.id)
                    val query = searchWelcomeTalk(group)
                    if (query == null) {
                        val talk = Answer(
                                atList = LinkedList(),
                                imgList = LinkedList(),
                                text = "欢迎来到 ${this.group.name}"
                            )
                        if (appendWelcomeTalk(group, talk)) {
                            reply("启动迎新成功！您可使用change指令修改迎新词")
                            return@route
                        }
                    }
                }
                reply("已开启迎新功能")
                return@route
            }

            case("change","修改迎新词"){
                if (searchWelcomeTalk(group)!=null &&
                    event.group.id in CommandGroupList.welcomeGroupList
                ){
                    SessionManager.addSession(
                        user = event.sender.id,
                        session = Session(
                            user = event.sender.id,
                            question = "",
                            type = "changeWelcome",
                            group = event.group.id
                        )
                    )
                    reply("请输入修改后的迎新词")
                }else{
                    reply("此群暂未开启迎新！")
                }
                return@route
            }
            case("help","获取指令"){
                reply(getHelp())
                return@route
            }
        }
        route(prefix = "", delimiter = " ")  {

            // 优先进行会话处理
            case("取消","停止会话录入"){
                if (SessionManager.hasSession(event.sender.id)) {
                    SessionManager.removeQuestion(event.sender.id)
                    SessionManager.removeSession(event.sender.id)
                    reply("取消会话成功！")
                }
            }
            if(!SessionManager.SessionsIsEmpty()) {
                if (SessionManager.performSession(event)) {
                    reply("录入成功！")
                    SessionManager.removeSession(event.sender.id)
                    return@route
                }else if(SessionManager.hasSession(event.sender.id)){
                    reply("格式有误！答案与问题不能相同，请重新检查录入答案格式或发送‘取消’停止当前对话")
                }
            }
            // 根据问题名称获取回答
            val tryGetAnswer = searchQuestion(
                    event.message[PlainText].toString()
                    , group.id)
                    ?.let {
                    getAnswer(it)
            }
            if (tryGetAnswer != null) {
                reply(tryGetAnswer)
                return@route
            }
            case("列表",desc = "获取此群的问题列表"){
                val query =  database
                        .from(Question)
                        .select()
                        .where {
                            (Question.group eq event.group.id)
                        }
                reply(buildString{
                    query.forEach {
                        append("#${it[Question.id]} ${it.get(Question.question)} \n")
                    }
                })
                return@route
            }
            if (event.group.id in CommandGroupList.managerGroupList){
                if (event.sender.permission.ordinal==0
                        && event.sender.id !=AppConfig.getInstance().SuperUser){
                    return@route
                }
            }
            case("添加问题","添加问题"){
                val question = event.message
                        .get(PlainText)?.contentToString()?.replace("添加问题","")
                        ?.replace(" ","")
                if (question!!.isEmpty()) return@route
                val furry =  Regex("""#\d""")
                if (furry.matches(question!!)){
                    reply("问题不能与索引重名")
                    return@case
                }else {
                    val query = question?.let {
                        searchQuestion(it, event.group.id)
                    }
                    if (query != null) {
                        reply("问题${query[Question.question]}已经存在")
                    } else {
                        DB.database.insert(Question) {
                            set(it.lastEditUser, event.sender.id)
                            set(it.group, event.sender.group.id)
                            set(it.question, question)
                        }
                        reply("问题${question}已被录入,请问如何回答?")
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
            case("修改问题","修改一个问题"){
                var question = event.message
                        .get(PlainText)?.toString()?.replace("修改问题","")
                        ?.replace(" ","")
                var query = question?.let {
                    searchQuestion(it, event.group.id)
                }
                if (query == null){
                    try {
                        val id = question?.replace("#", "")?.toInt()
                        query = quickSearchQuestion(id!!,group)
                        question = query?.get(Question.question)
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
                                    type = "changeUpDate",
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
                        .get(PlainText)?.contentToString()?.replace(" ","")
                        ?.replace("删除问题","")
                var query = searchQuestion(question!!,group.id)
                if (query==null){
                    val id = question?.replace("#", "")?.toInt()
                    query = quickSearchQuestion(id!!,group)
                }
                if (query!=null) {
                    deleteQuestion(query!!)
                    reply("已删除问题$question")
                }else{
                    reply("没有找到这个问题！")
                }
                return@route
            }
            furry("#","快速索引"){
                try {
                    val id = event.message[PlainText]?.contentToString()?.replace("#", "")?.toInt()
                    if (id!=null) {
                        val queryRowSet = quickSearchQuestion(id,group)
                        if (queryRowSet!=null) {
                            val tryAnswer = getAnswer(queryRowSet)
                            if (tryAnswer != null) {
                                reply(tryAnswer)
                                return@route
                            }
                        }else{
                            reply("此群不存在该序号的问题！")
                        }
                    }else{
                        throw NumberFormatException("参数错误！请输入问题序号")
                    }
                }catch (e:NumberFormatException){
                    logger.info(e)
                }catch (e:NullPointerException){
                    logger.info(e)
                }catch (e:Exception){
                    logger.info(e)
                }
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
                        if (searchQuestion(question= i[Question.question].toString(),groupID = event.group.id) ==null) {
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
            case("删除抽签","删除抽签"){
                DrawManager.deleteDraw(group.id)
                reply("删除成功！")
                return@route
            }
            case("创建抽签","创建抽签") {
                val signGroup = event.message
                        .get(PlainText)?.contentToString()?.replace("创建抽签", "")?.replace(" ","")
                if (signGroup != null) {
                    val numList = signGroup.split(",")
                    if (numList.size == 2) {
                        val star = numList[0].toInt()
                        val end = numList[1].toInt()
                        if(DrawManager.createDraw(group.id,star,end)){
                            reply("创建成功！")
                            return@route
                        }else{
                            reply("创建失败，或已有抽签活动在进行中")
                            return@route
                        }
                    }else{
                        reply("只能包括开始和结束两个参数！")
                    }
                }else{
                    reply("缺少参数！")
                }
                return@route
            }
            case("抽签","群内抽签设置"){
                val x = DrawManager.getInt(group.id)
                if (x==-1){
                    reply("暂无抽签活动或本次抽签已经结束！")
                    return@route
                }
                reply(At(event.sender).plus(x.toString()))
            }
            case("游戏推荐","游戏推荐"){
                if (event.group.id !in CommandGroupList.GameMorningGroupList)
                    return@route
                val GameIndex = Game.getGame()
                reply(buildString {
                        append("<%s>\n现价：%s\n评分：%s\n平台：%s\n点评：%s".format(
                                GameIndex.game.name,
                                GameIndex.game.price.current,
                                GameIndex.game.score,
                                GameIndex.game.gameType,
                                GameIndex.description))
                })
                return@route
            }
            case("涩图来","ST") {
                if (group.id in CommandGroupList.AnimationGroupList) {
                    Thread {
                        runBlocking {
                            val url = PicManager.getSTPic()
                            if (url == "") {
                                reply("太快了，休息一下吧")
                            } else {
                                try {
                                    PicManager.getStream()?.sendAsImage()
                                } catch (e: Exception) {
                                    Thread.sleep(500)
                                    PicManager.getStream()?.sendAsImage()
                                }
                            }
                        }
                    }.start()
                }
                return@route
            }
            case("图来","二次元图") {
                if (group.id in CommandGroupList.AnimationGroupList) {
                    Thread {
                        val url = PicManager.getPic()
                        runBlocking {
                            if (url == "") {
                                reply("太快了，休息一下吧")
                            } else {
                                try {
                                    URL(url).openConnection().getInputStream().sendAsImage()
                                }catch (e:Exception){
                                    Thread.sleep(500)
                                    URL(PicManager.getPic()).openConnection().getInputStream().sendAsImage()
                                }
                            }
                        }
                    }.start()
                    return@route
                }
            }
            case("法律","法律"){
                reply(FuckOkhttp("http://holk.tech:8886").getData())
            }
//            case("科普推荐","网易云热评"){
//                val data = FuckOkhttp(" https://v1.alapi.cn/api/comment").getData()
//                val hitokto = GsonBuilder().create().fromJson(data, Hitokto::class.java)
//
//            }

            case("帮助","获取帮助指令"){
                reply(getHelp())
                return@route
            }
            if (DisRepetition.thisMessageIsRepetition(event) && (group.id in CommandGroupList.DisRepetitionGroupList)){
                if (event.message[PlainText].toString()!=AppConfig.getInstance().DisRepetitionScence[0]) {
                    reply(AppConfig.getInstance().DisRepetitionScence[0])
                }else{
                    reply(AppConfig.getInstance().DisRepetitionScence[1])
                }
            }
        }
    }
}