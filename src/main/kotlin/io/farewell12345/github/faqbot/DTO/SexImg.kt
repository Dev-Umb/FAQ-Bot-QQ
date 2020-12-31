package io.farewell12345.github.faqbot.DTO


import com.google.gson.annotations.SerializedName

data class SexImg(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("quota")
    val quota: Int,
    @SerializedName("quota_min_ttl")
    val quotaMinTtl: Int
) {
    data class Data(
        @SerializedName("author")
        val author: String,
        @SerializedName("height")
        val height: Int,
        @SerializedName("p")
        val p: Int,
        @SerializedName("pid")
        val pid: Int,
        @SerializedName("r18")
        val r18: Boolean,
        @SerializedName("tags")
        val tags: List<String>,
        @SerializedName("title")
        val title: String,
        @SerializedName("uid")
        val uid: Int,
        @SerializedName("url")
        val url: String,
        @SerializedName("width")
        val width: Int
    )
}