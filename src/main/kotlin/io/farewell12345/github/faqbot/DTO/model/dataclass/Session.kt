package io.farewell12345.github.faqbot.DTO.model.dataclass

data class Session(
    val group:Long,
    val user:Long,
    val question:String,
    val type:String
)
