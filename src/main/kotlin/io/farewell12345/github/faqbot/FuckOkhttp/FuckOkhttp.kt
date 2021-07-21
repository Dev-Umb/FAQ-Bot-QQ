package io.farewell12345.github.faqbot.FuckOkhttp

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import io.farewell12345.github.faqbot.DTO.model.NucUser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class FuckOkhttp() {
    private val client = OkHttpClient()

    class VerifyBaseModel{
        @SerializedName("code")
        val code: Int = 0

        @SerializedName("success")
        val success: Boolean = false
        @SerializedName("text")
        val text: String? = null
        @SerializedName("data")
        val data: HashMap<String,Any>? = null

        @SerializedName("isFake")
        val isFake: Boolean = false
    }
    public val JSON: MediaType? = "application/json".toMediaTypeOrNull()
    inline fun <reified T> post(url: String, body:String?=null):T{
        val client= OkHttpClient()
        val sendBody = body!!.toRequestBody(JSON);
        val requestBody = Request.Builder()
            .url(url)
            .post(sendBody)
            .build()
        val responseBody = client.newCall(requestBody)
                .execute().body?.string()
        return GsonBuilder().create()
            .fromJson(responseBody, T::class.java)
    }

    fun postFile(url: String?,file: ByteArray): Response? {
        val mediaType: MediaType = "image/png".toMediaTypeOrNull()!!
        val fileBody = RequestBody.create(mediaType, file)
        val request = url?.let {
            Request.Builder()
                .url(it).post(fileBody)
                .build()
        }
        return request?.let { client.newCall(it).execute() }
    }
}