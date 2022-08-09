package ink.umb.faqbot.dto.model.bind

import me.liuwj.ktorm.entity.Entity

interface QuestionBind: Entity<QuestionBind> {
    companion object : Entity.Factory<QuestionBind>()
    val id:Int
    var question: MessageBind
    var group:Long
    var answer: MessageBind
    var lastEditUser:String
}
