package umb.ink.ktor.data.bind

import org.ktorm.entity.Entity

interface MessageBind: BaseBind<MessageBind> {
    companion object: Entity.Factory<MessageBind>()
    var data:String
    var questionId:Int
    var type: Int
    var sender: Long
}