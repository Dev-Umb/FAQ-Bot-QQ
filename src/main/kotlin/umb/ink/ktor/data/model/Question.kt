package umb.ink.ktor.data.model

import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import umb.ink.ktor.config.DBConfig
import umb.ink.ktor.data.bind.QuestionBind
import umb.ink.ktor.data.model.msg.QAMessage

object Question: Table<QuestionBind>("question") {
    val id = int("id").primaryKey().bindTo { it.id }
    val question = int("question_id").references(QAMessage){ it.question }
    val answer= int("answer_id").references(QAMessage){ it.answer }
    val group= long("group").bindTo { it.group }
    val lastEditUser= varchar("last_edit_user").bindTo { it.lastEditUser }
    val Database.question get() = sequenceOf(Question)
    val Database.message get() = sequenceOf(QAMessage)
}
