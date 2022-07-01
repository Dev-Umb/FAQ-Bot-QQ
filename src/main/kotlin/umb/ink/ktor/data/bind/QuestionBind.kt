package umb.ink.ktor.data.bind

import org.ktorm.entity.Entity

interface QuestionBind: BaseBind<QuestionBind> {
    companion object : Entity.Factory<QuestionBind>()
    var question:MessageBind
    var group:Long
    var answer:MessageBind
    var lastEditUser:String
}