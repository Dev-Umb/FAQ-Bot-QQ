package FuckOkhttp

import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.DTO.XiaoHeiHe
import okhttp3.OkHttpClient
import okhttp3.Request

class FuckOkhttp(url:String?){
    val url = url
    val client=OkHttpClient()
    fun getData(): String {
            val request = this.url?.let {
                Request.Builder()
                    .url(it).get()
                    .build()
            }
            val responseGet= request?.let { client.newCall(it).execute() }
            return responseGet?.body?.string().toString()
    }
}

fun main() {
    val data = FuckOkhttp("https://api.xiaoheihe.cn/game/web/all_recommend/?os_type=web&version=999.0.0&hkey=1f91644ab2fe0ad174f345ccb25282c3&_time=\"+Date().time").getData()
    var GameIndexs = GsonBuilder().create().fromJson(data, XiaoHeiHe::class.java)
    val GameIndex = GameIndexs.result.list
    print(GameIndex[0].game.name)
}