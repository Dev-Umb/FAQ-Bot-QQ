package io.farewell12345.github.faqbot.DTO.model.QAmodel

import io.farewell12345.github.faqbot.DTO.model.QAmodel.Bind.QuestionBind
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Message.bindTo
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object Question: Table<QuestionBind>("question") {
    val Database.question get() = sequenceOf(Question)
    val Database.message get() = sequenceOf(Message)
    val id = int("id").primaryKey().bindTo { it.id }
    val question = int("question_id").references(Message){ it.question }
    val answer=int("answer_id").references(Message){ it.answer }
    val group= long("group").bindTo { it.group }
    val lastEditUser=varchar("last_edit_user").bindTo { it.lastEditUser }
}