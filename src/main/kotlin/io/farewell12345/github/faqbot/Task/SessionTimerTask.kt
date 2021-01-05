package io.farewell12345.github.faqbot.Task

import java.util.*
import kotlin.collections.HashMap


class TimerSessionManager(): TimerTask(){ // 时间调度器
    private var UserSessionManager: HashMap<Long,Long> = HashMap() // 用户调用队列
    override fun run() {

    }
    fun CanUseBot(id:Long): Boolean {
        if (id !in UserSessionManager.keys) {
            return flushUser(id)
        }
        return (Date().time - UserSessionManager[id]!!)/2000 > 4
    }
    fun flushUser(id: Long): Boolean {
        UserSessionManager[id] = Date().time
        return true
    }
}