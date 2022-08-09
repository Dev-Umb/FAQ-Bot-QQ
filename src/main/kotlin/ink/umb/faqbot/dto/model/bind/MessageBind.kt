package ink.umb.faqbot.dto.model.bind

import me.liuwj.ktorm.entity.Entity

interface MessageBind: Entity<MessageBind> {
    companion object : Entity.Factory<MessageBind>()
    var id:Int
    var data:String
    var questionId:Int
}