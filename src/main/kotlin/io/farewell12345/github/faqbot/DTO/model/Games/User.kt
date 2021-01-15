package io.farewell12345.github.faqbot.DTO.model.Games

import io.farewell12345.github.faqbot.DTO.model.Games.Game.bindTo
import io.farewell12345.github.faqbot.DTO.model.Games.Game.primaryKey
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long

object User: Table<Nothing>("user") {
    val id = int("id").primaryKey()
    val qq = long("qq")
}