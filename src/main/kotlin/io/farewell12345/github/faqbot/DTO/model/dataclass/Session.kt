package io.farewell12345.github.faqbot.DTO.model.dataclass

import net.mamoe.mirai.contact.Group

data class Session(
    val group: Group,
    val user:Long,
    val question:String = "",
    val type:String
)
