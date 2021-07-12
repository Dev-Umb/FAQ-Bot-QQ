package io.farewell12345.github.faqbot.Plugin.TimerTask

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.MessageChain
import java.util.*


object TaskManage {
    @OptIn(DelicateCoroutinesApi::class)
    fun addTask(group: Group, messageChain: MessageChain, date: Date, intervalTime: Long) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                println("定时！")
                GlobalScope.launch {
                    group.sendMessage(messageChain)
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