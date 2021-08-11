package io.farewell12345.github.faqbot.DTO.model.QAmodel.Bind

import io.farewell12345.github.faqbot.DTO.model.QAmodel.Message.primaryKey
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Question
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

interface MessageBind: Entity<MessageBind> {
    companion object : Entity.Factory<MessageBind>()
    var id:Int
    var data:String
    var questionId:Int
}