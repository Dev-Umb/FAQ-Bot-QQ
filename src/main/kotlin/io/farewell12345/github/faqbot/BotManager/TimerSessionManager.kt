package io.farewell12345.github.faqbot.BotManager

import java.util.*
import kotlin.collections.HashMap


class TimerSessionManager(): TimerTask(){ // 时间调度器
    private var userSessionManager: HashMap<Long,Long> = HashMap() // 用户调用队列
    override fun run() {

    }
    fun canUseBot(id:Long): Boolean {
        if (id !in userSessionManager.keys) {
            return flushUser(id)
        }
        return (Date().time - userSessionManager[id]!!)/2000 > 2
    }
    fun flushUser(id: Long): Boolean {
        userSessionManager[id] = Date().time
        return true
    }
}