package io.farewell12345.github.faqbot.DTO.model.QAmodel.Games

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int

object GameAndUserRemote:Table<Nothing>("game_user_remote") {
    val userId = int("user_id")
    val gameId = int("game_id")
}