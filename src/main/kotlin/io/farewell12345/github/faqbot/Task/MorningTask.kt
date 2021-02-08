package io.farewell12345.github.faqbot.Task

import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.BotManager.BotsManager
import io.farewell12345.github.faqbot.BotManager.CommandGroupList
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import java.util.*



class GameMorningTask : TimerTask(){
    val task = GameMorningTask()
    override fun run() {
        runBlocking {
            for (i in CommandGroupList.gameMorningGroupList) {
                BotsManager.oneBot?.getGroup(i)?.sendMessage("hello!")
            }
        }
    }
}