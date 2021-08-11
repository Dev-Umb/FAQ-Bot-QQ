package io.farewell12345.github.faqbot.DTO.model.QAmodel.Bind

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.entity.sequenceOf

interface QuestionBind: Entity<QuestionBind> {
    companion object : Entity.Factory<QuestionBind>()
    val id:Int
    var question:MessageBind
    var group:Long
    var answer:MessageBind
    var lastEditUser:Long
}
