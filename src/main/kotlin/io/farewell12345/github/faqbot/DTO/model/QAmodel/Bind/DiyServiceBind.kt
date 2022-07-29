package io.farewell12345.github.faqbot.DTO.model.QA

import io.farewell12345.github.faqbot.DTO.model.QAmodel.Bind.MessageBind

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Column

interface DiyServiceBind: Entity<DiyServiceBind> {
    @Target(AnnotationTarget.TYPE)
    annotation class MatchMode{
        companion object{
            const val obscure: Int = 0
            const val precise: Int = 1
            const val all: Int = 2
        }
    }
    companion object : Entity.Factory<DiyServiceBind>()
    var id:Int
    var command: String
    var mode: @MatchMode Int
    var url: String
    // 服务响应头需要解析的字段列表
    var data: String
}