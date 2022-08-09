package ink.umb.faqbot.bot.manager

import ink.umb.faqbot.controller.LineArtController
import ink.umb.faqbot.controller.QuestionController
import ink.umb.faqbot.controller.TaskerTimeController
import ink.umb.faqbot.controller.WelcomeController
import ink.umb.faqbot.dto.model.dataclass.Session
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.MiraiInternalApi


object SessionManager {
    private var Sessions = mutableMapOf<Long, Session>()

    fun hasSession(id: Long): Boolean {
        return id in Sessions.keys
    }

    fun removeQuestion(id: Long) {
        if (Sessions[id]?.type == "addUpDate") {
            QuestionController.deleteQuestion(
                QuestionController.searchQuestion(
                    Sessions[id]!!.data,
                    Sessions[id]!!.group
                )!!
            )
        }
    }

    fun removeSession(id: Long) {
        Sessions.remove(id)
    }

    fun sessionsIsEmpty(): Boolean {
        return Sessions.isEmpty()
    }

    fun addSession(user: Long, session: Session) {
        Sessions[user] = session
    }

    @OptIn(MiraiInternalApi::class)
    fun performSession(messageEvent: FriendMessageEvent): Boolean {
        var flag = false
        val session = Sessions[messageEvent.sender.id]
        removeSession(messageEvent.sender.id)
        if (session != null) {
            if (messageEvent.message.filterIsInstance<PlainText>().firstOrNull()?.content?.replace(
                    " ",
                    ""
                ) == session.data
            ) {
                return false
            }
            flag = true
            when (session.type) {
                "timerTask" -> {
                    val atAll = (session.data == "@ALL")
                    val time = TaskerTimeController.newTaskTimer(messageEvent.message, session.group,atAll)
                    GlobalScope.launch {
                        messageEvent.friend.sendMessage("已设置时间为${time?.time} 间隔${24}h")
                    }
                    return true
                }
            }
        }
        return flag
    }

    @MiraiInternalApi
    fun performSession(messageEvent: GroupMessageEvent): Boolean {
        var flag = false
        val session = Sessions[messageEvent.sender.id]
        removeSession(messageEvent.sender.id)
        if (session != null) {
            if (messageEvent.message.filterIsInstance<PlainText>().firstOrNull()?.content?.replace(
                    " ",
                    ""
                ) == session.data
            ) {
                return false
            }
            flag = true
            when (session.type) {
                "addUpDate" ->
                    return QuestionController.upDateQuestionAnswer(messageEvent, session)
                "changeUpDate" ->
                    return QuestionController.upDateQuestionAnswer(messageEvent, session)
                "changeWelcome" ->
                    return WelcomeController.changeWelcome(
                        messageEvent.group,
                        messageEvent.message
                    )
                "LineArt"->
                    return LineArtController.getLineArtImg(messageEvent)

            }

        }
        return flag
    }
}

fun removeSession(sender: Long) {
    SessionManager.removeQuestion(sender)
    SessionManager.removeSession(sender)
}
