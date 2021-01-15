package io.farewell12345.github.faqbot.DTO.model.Games

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar


object Game: Table<Nothing>("game") {
    val id = int("id").primaryKey()
    val group = long("group")
    val name = varchar("name")
}