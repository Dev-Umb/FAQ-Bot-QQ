package ink.umb.faqbot.plugin.TimerTask

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.AtAll
import net.mamoe.mirai.message.data.MessageChain
import java.util.*


object TaskManage {
    fun addTask(group: Group, messageChain: MessageChain, date: Date, intervalTime: Long,
                atALL:Boolean = false) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                println("定时！")
                GlobalScope.launch {
                    group.sendMessage(messageChain)
                    if(atALL) group.sendMessage(AtAll)
                }
            }
        }, date, intervalTime)

    }
}

fun main() {
    Timer().schedule(object : TimerTask() {
        override fun run() {
            println("1111")
        }
    }, Date(), 1000)
    Timer().schedule(object : TimerTask() {
        override fun run() {
            println("2221")
        }
    }, Date(), 1000)
}