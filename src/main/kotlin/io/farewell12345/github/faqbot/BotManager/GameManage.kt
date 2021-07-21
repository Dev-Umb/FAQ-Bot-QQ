package io.farewell12345.github.faqbot.BotManager

import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.DTO.model.dataclass.XiaoHeiHe
import java.util.*

//object GameManage{
//    private var Time = Date().time
//    private val url = "https://api.xiaoheihe.cn/game/web/all_recommend/game_comments/?os_type=web&version=999.0.0&hkey=c348018bc694bf3c962d5fbe08f544a2&_time=$Time&limit=15&offset=0"
//    private var data = FuckOkhttp(url).getData()
//    private var GameIndex = GsonBuilder().create().fromJson(data, XiaoHeiHe::class.java).result.list
//    fun getGames(): List<XiaoHeiHe.Result.Gamer> {
//        if (Date().time - Time >= 60*60*1000){
//            Time = Date().time
//            data = FuckOkhttp(url).getData()
//            GameIndex = GsonBuilder().create().fromJson(data, XiaoHeiHe::class.java).result.list
//        }
//        return GameIndex
//    }
//    fun getGame(): XiaoHeiHe.Result.Gamer {
//        return getGames()[(GameIndex.indices).random()]
//    }
//}