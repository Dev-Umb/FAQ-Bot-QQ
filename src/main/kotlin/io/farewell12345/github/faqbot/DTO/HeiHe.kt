package io.farewell12345.github.faqbot.DTO


import com.google.gson.annotations.SerializedName

data class HeiHe(
        @SerializedName("code")
    val code: Int,
        @SerializedName("items")
    var items: List<Item>,
        @SerializedName("msg")
    val msg: String
) {
    data class Item(
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("score")
        val score: String,
        @SerializedName("type")
        val type: String
    )
}