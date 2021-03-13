package io.farewell12345.github.faqbot.Listener

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import io.farewell12345.github.faqbot.BotManager.SessionManager
import io.farewell12345.github.faqbot.DTO.Controller.WelcomeController
import io.farewell12345.github.faqbot.DTO.model.dataclass.Answer
import io.farewell12345.github.faqbot.DTO.model.dataclass.Session
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.MiraiInternalApi
import java.util.*

class BotGroupCommandListener:BaseListeners() {
    @MiraiInternalApi
    @EventHandler
    suspend fun GroupMessageEvent.onEvent() {
        route(prefix = ".command", delimiter = " ") {
            if (event.sender.permission.ordinal == 0
                && event.sender.id != AppConfig.getInstance().superUser
            ) {
                return@route
            }
            case("addGame", "开启游戏推荐",false) {
                if (event.group.id !in CommandGroupList.gameMorningGroupList) {
                    CommandGroupList.gameMorningGroupList.add(event.group.id)
                }
                subject.sendMessage("本群游戏推荐已开启")
                return@route
            }
            case("closeGame", "关闭游戏推荐",false) {
                if (event.group.id in CommandGroupList.gameMorningGroupList) {
                    CommandGroupList.gameMorningGroupList.remove(event.group.id)
                }
                subject.sendMessage("本群游戏推荐已关闭")
                return@route
            }
            case("Dis", "开启反复读",false) {
                if (event.group.id !in CommandGroupList.disRepetitionGroupList) {
                    CommandGroupList.disRepetitionGroupList.add(event.group.id)
                }
                subject.sendMessage("反复读已开启")
                return@route
            }
            case("closeDis", "关闭反复读",false) {
                if (event.group.id in CommandGroupList.disRepetitionGroupList) {
                    CommandGroupList.disRepetitionGroupList.remove(event.group.id)
                }
                subject.sendMessage("反复读已关闭")
                return@route
            }
            case("Animation", "开启发图",false) {
                if (event.group.id !in CommandGroupList.animationGroupList) {
                    CommandGroupList.animationGroupList.add(event.group.id)
                }
                subject.sendMessage("图片发送已开启")
                return@route
            }
            case("closeAnim", "关闭反复读",false) {
                if (event.group.id in CommandGroupList.animationGroupList) {
                    CommandGroupList.animationGroupList.remove(event.group.id)
                }
                subject.sendMessage("图片发送已关闭")
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
                if (event.group.id !in CommandGroupList.welcomeGroupList) {
                    CommandGroupList.managerGroupList.add(event.group.id)
                }
                subject.sendMessage("开启词条管理！")
                return@route
            }
            case("dismanage", "关闭仅限管理员进行操作",false) {
                if (event.group.id in CommandGroupList.welcomeGroupList) {
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
                            question = "",
                            type = "changeWelcome",
                            group = event.group.id
                        )
                    )
                    subject.sendMessage("请输入修改后的迎新词")
                } else {
                    subject.sendMessage("此群暂未开启迎新！")
                }
                return@route
            }
            case("help", "获取指令",false) {
                subject.sendMessage(getHelp())
                return@route
            }
        }
    }
}