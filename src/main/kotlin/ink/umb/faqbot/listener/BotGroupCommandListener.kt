package ink.umb.faqbot.listener

import ink.umb.faqbot.AppConfig
import ink.umb.faqbot.bot.manager.CommandGroupList
import ink.umb.faqbot.bot.manager.SessionManager
import ink.umb.faqbot.controller.DiyServiceController
import ink.umb.faqbot.controller.WelcomeController
import ink.umb.faqbot.dto.model.dataclass.Answer
import ink.umb.faqbot.dto.model.dataclass.Session
import ink.umb.faqbot.process.FuckSchoolSisterUtil
import ink.umb.faqbot.route.IMessageEvent
import ink.umb.faqbot.route.route
import io.farewell12345.github.faqbot.dto.model.QA.MatchMode
import io.farewell12345.github.faqbot.dto.model.QA.RequestMethod
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import java.util.*

class BotGroupCommandListener: BaseListeners(), IMessageEvent {
    @EventHandler
    override suspend fun GroupMessageEvent.onEvent() {
        route(prefix = ".command", delimiter = " ") {
            if (event.sender.permission.ordinal == 0
                && event.sender.id != AppConfig.getInstance().superUser
            ) {
                return@route
            }
            case("closeWel", "关闭迎新",false) {
                if (event.group.id in CommandGroupList.welcomeGroupList) {
                    CommandGroupList.welcomeGroupList.remove(event.group.id)
                }
                subject.sendMessage("本群迎新功能已关闭")
                return@route
            }
            case("restartPsSister", "重启服务，仅限超级管理员"){
                if(sender.id == AppConfig.getInstance().superUser){
                    FuckSchoolSisterUtil.restart()
                    subject.sendMessage("重启成功")
                }else{
                    subject.sendMessage("您没有权限，请联系开发者")
                }
            }
            case("add","添加"){
                val reply = message.get(QuoteReply.Key)
                val data = reply?.source?.originalMessage?.contentToString()
                if (data!!.length > 10){
                    launch {
                        FuckSchoolSisterUtil.addDataSet(data)
                        FuckSchoolSisterUtil.train()
                        subject.sendMessage("模型迭代完成")
                    }
                    subject.sendMessage("添加语料成功,正在迭代模型")
                }else{
                    subject.sendMessage("语料文本太短，不足以支持训练")
                }
            }
            case("fuckPsSister","开启/关闭反PS学姐功能"){
                if (event.group.id !in CommandGroupList.fuckPsSister){
                    FuckSchoolSisterUtil.destroy()
                    FuckSchoolSisterUtil.init()
                    CommandGroupList.fuckPsSister.add(group.id)
                    subject.sendMessage("已开启反PS学姐功能，将实时监听群内消息")
                }else{
                    CommandGroupList.fuckPsSister.remove(group.id)
                    subject.sendMessage("已关闭反PS学姐功能")
                }
            }
            case("manage", "仅限管理员进行操作",false) {
                if (event.group.id !in CommandGroupList.managerGroupList) {
                    CommandGroupList.managerGroupList.add(event.group.id)
                }
                subject.sendMessage("开启词条管理！")
                return@route
            }
            case("fakeInfo", "仅限管理员进行操作",false){
                if (CommandGroupList.fakeInfoIdentityHashMap[event.group.id] != true) {
                    CommandGroupList.fakeInfoIdentityHashMap[event.group.id] = true
                }
                subject.sendMessage("开启虚假信息监测！")
            }
            case("closeFakeInfo", "仅限管理员进行操作",false){
                if (CommandGroupList.fakeInfoIdentityHashMap[event.group.id] == true) {
                    CommandGroupList.fakeInfoIdentityHashMap[event.group.id] = false
                }
                subject.sendMessage("开启虚假信息监测！")
            }
            case("dismanage", "关闭仅限管理员进行操作",false) {
                if (event.group.id in CommandGroupList.managerGroupList) {
                    CommandGroupList.managerGroupList.remove(event.group.id)
                }
                subject.sendMessage("关闭词条管理！")
                return@route
            }
            case("welcome", "开启迎新词",false) {
                if (event.group.id !in CommandGroupList.welcomeGroupList) {
                    CommandGroupList.welcomeGroupList.add(this.group.id)
                    val query =WelcomeController.searchWelcomeTalk(group)
                    if (query == null) {
                        val talk = Answer(
                            atList = LinkedList(),
                            imgList = LinkedList(),
                            text = "欢迎来到 ${this.group.name}"
                        )
                        if (WelcomeController.appendWelcomeTalk(group, talk)) {
                            subject.sendMessage(PlainText("启动迎新成功！您可使用change指令修改迎新词"))
                            return@route
                        }
                    }
                }

                subject.sendMessage("已开启迎新功能")
                return@route
            }
            case("change", "修改迎新词",false) {
                if (WelcomeController.searchWelcomeTalk(group) != null &&
                    event.group.id in CommandGroupList.welcomeGroupList
                ) {
                    SessionManager.addSession(
                        user = event.sender.id,
                        session = Session(
                            user = event.sender.id,
                            data = "",
                            type = "changeWelcome",
                            group = event.group
                        )
                    )
                    subject.sendMessage("请输入修改后的迎新词")
                } else {
                    subject.sendMessage("此群暂未开启迎新！")
                }
                return@route
            }
            case("lucky","抽签功能"){
                    if (!CommandGroupList.luckyGroup.containsKey(event.group.id)){
                        CommandGroupList.luckyGroup[event.group.id] = true;
                        subject.sendMessage("开启抽签功能")
                        return@route
                    }
                    CommandGroupList.luckyGroup[event.group.id] = !CommandGroupList.luckyGroup[event.group.id]!!
                    subject.sendMessage("开启抽签功能")
            }
            case("addService"){
                val args: List<String> =
                    message.findIsInstance<PlainText>()!!
                        .content
                        .replace(".command ", "")
                        .replace("addService ", "")
                        .split(" ")
                val pattern = args[0] ?: launch{
                    subject.sendMessage("缺少匹配指令")
                    throw DiyServiceController.NoArgsException()
                }
                val url = args[1] ?: launch{
                    subject.sendMessage("缺少服务端地址")
                    throw DiyServiceController.NoArgsException()
                }
                val requestMode = when(args[2]){
                    "get" -> RequestMethod.get
                    "post" -> RequestMethod.post
                    else -> {
                        RequestMethod.get
                    }
                }
                if (pattern is String && url is String){
                    DiyServiceController.addService(
                        pattern = pattern,
                        url = url,
                        patternMode = MatchMode.precise,
                        requestMethod = requestMode
                    )
                }
                subject.sendMessage("service 添加成功")
            }
            case("service"){
                val serviceList = DiyServiceController.getAllService().joinToString("\n")
                subject.sendMessage(serviceList)
            }
            case("help", "获取指令",false) {
                subject.sendMessage(getHelp())
                return@route
            }
        }
    }

}