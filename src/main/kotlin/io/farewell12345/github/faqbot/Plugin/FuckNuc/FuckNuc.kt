package io.farewell12345.github.faqbot.Plugin.FuckNuc

import com.google.gson.GsonBuilder
import io.farewell12345.github.faqbot.AppConfig
import io.farewell12345.github.faqbot.DTO.model.NucUser
import io.farewell12345.github.faqbot.FuckOkhttp.FuckOkhttp
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request


fun sign(id: String): String {
    return FuckOkhttp(AppConfig.getInstance().getStudentUrl(id)).getNuc().msg
}
