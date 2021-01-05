package io.farewell12345.github.faqbot.DTO.model.dataclass


import com.google.gson.annotations.SerializedName

data class Hitokto(
    @SerializedName("author")
    val author: Author,
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String
) {
    data class Author(
        @SerializedName("desc")
        val desc: String,
        @SerializedName("name")
        val name: String
    )

    data class Data(
        @SerializedName("creator")
        val creator: String,
        @SerializedName("from")
        val from: String,
        @SerializedName("hitokoto")
        val hitokoto: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("type")
        val type: String
    )
}