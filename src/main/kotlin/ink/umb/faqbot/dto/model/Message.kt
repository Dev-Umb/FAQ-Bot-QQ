package ink.umb.faqbot.dto.model

import ink.umb.faqbot.dto.model.bind.MessageBind
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
object Message:Table<MessageBind>("message")  {
    var id = int("id").primaryKey().bindTo { it.id }
    var data = varchar("data").bindTo { it.data }
    var questionId = int("question_id").bindTo { it.questionId }
}