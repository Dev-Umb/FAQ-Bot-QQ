package io.farewell12345.github.faqbot.FuckOkhttp

import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.DTO.model.dataclass.XiaoHeiHe
import okhttp3.OkHttpClient
import okhttp3.Request

class FuckOkhttp(private val url: String?){
    private val client=OkHttpClient()
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