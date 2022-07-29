package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.BotManager.SessionManager
import io.farewell12345.github.faqbot.Controller.WelcomeController
import io.farewell12345.github.faqbot.DTO.model.dataclass.Answer
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.MiraiInternalApi
import java.util.*

class BotGroupCommandListener: BaseListeners() {
    @MiraiInternalApi
    @EventHandler
    suspend fun GroupMessageEvent.onEvent() {
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
            case("help", "获取指令",false) {
                subject.sendMessage(getHelp())
                return@route
            }
        }
    }

}