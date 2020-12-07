package FuckOkhttp

import com.google.gson.GsonBuilder
import java.util.*

object Game{
    private var Time = Date().time
    private val url = "https://api.xiaoheihe.cn/game/web/all_recommend/?os_type=web&version=999.0.0&hkey=1f91644ab2fe0ad174f345ccb25282c3&_time=$Time"
    private var data = FuckOkhttp(url).getData()
    private var GameIndex = GsonBuilder().create().fromJson(data, XiaoHeiHe::class.java).result.overview
    fun getGames(): List<XiaoHeiHe.Result.Overview> {
        if (Date().time - Time >= 24*60*60*1000){
            Time = Date().time
            data = FuckOkhttp(url).getData()
            GameIndex = GsonBuilder().create().fromJson(data, XiaoHeiHe::class.java).result.overview
        }
        return GameIndex
    }
}