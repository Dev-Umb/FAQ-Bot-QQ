package io.farewell12345.github.faqbot.DTO.model

import java.util.*

data class Answer (
        val imgList : LinkedList<String>,
        val atList : LinkedList<Long>,
        val text : String
)