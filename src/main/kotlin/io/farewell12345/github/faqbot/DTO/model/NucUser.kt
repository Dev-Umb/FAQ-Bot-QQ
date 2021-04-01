package io.farewell12345.github.faqbot.DTO.model


import com.google.gson.annotations.SerializedName

data class NucUser(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Any>,
    @SerializedName("msg")
    val msg: String
)