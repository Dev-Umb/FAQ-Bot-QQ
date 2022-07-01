package umb.ink.ktor.data.model.msg
import org.ktorm.schema.*
import umb.ink.ktor.data.bind.MessageBind

object QAMessage: Table<MessageBind>("message"), Message {
    override var id = int("id").primaryKey().bindTo { it.id }
    override var data = varchar("data").bindTo { it.data }
    override var sender: Column<Long> = long("sender").bindTo { it.sender }
    override var group: Column<Long>? = null
    var questionId = int("question_id").bindTo { it.questionId }
    var type = int("type").bindTo { it.type }
}
