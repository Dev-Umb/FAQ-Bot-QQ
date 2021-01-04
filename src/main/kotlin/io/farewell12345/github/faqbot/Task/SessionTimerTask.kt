package io.farewell12345.github.faqbot.Task

import io.farewell12345.github.faqbot.BotManager.SessionManager
import java.util.*



class TimerSessionManager(): TimerTask(){
    private var UserSessionManager: ArrayDeque<Long> = ArrayDeque() // 用户调用队列
    override fun run() {
        if (UserSessionManager.isEmpty()){
            return
        }
        UserSessionManager.pollFirst()
    }
    fun CanUseBot(id:Long): Boolean {
        return !(id in UserSessionManager)
    }
    fun addUser(id: Long){
        if (id in UserSessionManager){
            return
        }
        UserSessionManager.push(id)
    }
}