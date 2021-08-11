package io.farewell12345.github.faqbot.DTO.model.QAmodel

import io.farewell12345.github.faqbot.DTO.DB.DB
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Bind.MessageBind
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Bind.QuestionBind
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question.question
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.toList
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar
object Message:Table<MessageBind>("message")  {
    var id = int("id").primaryKey().bindTo { it.id }
    var data = varchar("data").bindTo { it.data }
    var questionId = int("question_id").bindTo { it.questionId }
}