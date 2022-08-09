package ink.umb.faqbot.fuck.http

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import ink.umb.faqbot.dto.db.logger
import ink.umb.faqbot.dto.model.dataclass.FuckSisterResponse

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class FuckOkhttp(private val url:String?=null) {
    private val client = OkHttpClient()
    private val logger = logger()
    fun reportLogger(url:String){
        logger.debug(url)
    }
    fun getBytes(): ByteArray? {
        val request = this.url?.let {
            Request.Builder()
                .url(it).get()
                .build()
        }
        val responseGet = request?.let { client.newCall(it).execute() }
        return responseGet?.body?.bytes()
    }
    fun get(): String? {
        val request = this.url?.let {
            Request.Builder()
                .url(it).get()
                .build()
        }
        val responseGet = request?.let { client.newCall(it).execute() }

        return responseGet?.body?.string().also {
            if (url != null) {
                reportLogger("$url:$it")
            }
        }
    }
    fun postFile(file: ByteArray): Response? {
        val mediaType: MediaType = "image/png".toMediaTypeOrNull()!!
        val fileBody = RequestBody.create(mediaType, file)
        val request = this.url?.let {
            Request.Builder()
                .url(it).post(fileBody)
                .build()
        }
        return request?.let { client.newCall(it).execute() }
    }
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
        reportLogger(url)
        return GsonBuilder().create()
            .fromJson(responseBody, T::class.java)
    }

    fun postFuckSister(body: String): FuckSisterResponse? {
        val client= OkHttpClient()
        val builder = FormBody.Builder()
        builder.add("text",body)
        val fromBody = builder.build()
        val request = url?.let {
            Request.Builder()
                .url(it)
                .post(fromBody)
                .build()
        }
        val responseBody = request?.let {
            client.newCall(it)
                .execute().body?.string()
        }
        url?.let { reportLogger(it) }
        return GsonBuilder().create()
            .fromJson(responseBody, FuckSisterResponse::class.java)
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