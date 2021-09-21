package io.farewell12345.github.faqbot.Controller

import io.farewell12345.github.faqbot.Plugin.TimerTask.TaskManage
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.MessageChain
import java.util.*

object TaskerTimeController {
    fun newTaskTimer(messageChain: MessageChain,group: Group,atAll:Boolean): Calendar? {
        val now = Calendar.getInstance()
        now.set(Calendar.HOUR_OF_DAY, 8); 	// 控制时
        now.set(Calendar.MINUTE, 0);       // 控制分
        now.set(Calendar.SECOND, 0);       // 控制秒
        //这里我没有控制是哪一天，默认为当天
        TaskManage.addTask(group,messageChain, now.time, 1000*60*60*24,atAll)
        return now
    }
}