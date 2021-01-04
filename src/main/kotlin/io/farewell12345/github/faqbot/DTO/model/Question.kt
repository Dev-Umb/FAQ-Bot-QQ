package io.farewell12345.github.faqbot.DTO.model

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object Question: Table<Nothing>("question") {
    val id = int("id").primaryKey()
    val question = varchar("question")
    val group=long("group_id")

    val answer=varchar("answer")

    val lastEditUser=long("last_edit_user")
}