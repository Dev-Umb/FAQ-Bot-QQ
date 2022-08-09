package ink.umb.faqbot.dto.model


import ink.umb.faqbot.dto.model.bind.QuestionBind
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
    var answer=int("answer_id").references(Message){ it.answer }
    val group= long("group_id").bindTo { it.group }
    val lastEditUser=varchar("last_edit_user").bindTo { it.lastEditUser }
}