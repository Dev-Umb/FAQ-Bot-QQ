package io.farewell12345.github.faqbot.DTO.model.QAmodel.Games

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long

object User: Table<Nothing>("user") {
    val id = int("id").primaryKey()
    val qq = long("qq")
}