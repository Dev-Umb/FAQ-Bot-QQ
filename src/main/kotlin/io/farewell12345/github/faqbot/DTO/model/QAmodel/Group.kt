package io.farewell12345.github.faqbot.DTO.model.QAmodel

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object Group: Table<Nothing>("group") {
    val groupID = long("group")
    val user_id = long("user_id")
}