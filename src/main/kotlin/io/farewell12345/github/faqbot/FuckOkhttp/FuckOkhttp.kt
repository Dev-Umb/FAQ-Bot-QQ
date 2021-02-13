package io.farewell12345.github.faqbot.FuckOkhttp

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File


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
    fun postFile(file: ByteArray): Response? {
        val mediaType: MediaType = "image/png".toMediaTypeOrNull()!!
        val fileBody = RequestBody.create(mediaType,file)
        val request = this.url?.let {
            Request.Builder()
                .url(it).post(fileBody)
                .build()
        }
        return request?.let { client.newCall(it).execute() }
    }
}