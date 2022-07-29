package io.farewell12345.github.faqbot.DTO.model.QAmodel

import io.farewell12345.github.faqbot.DTO.model.QA.DiyServiceBind
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Message.bindTo
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Message.primaryKey
import me.liuwj.ktorm.schema.Column
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

object DiyService: Table<DiyServiceBind>("service") {
    var id = int("id").primaryKey().bindTo { it.id }

    var command = varchar("command").bindTo { it.command }
    var mode = int("mode").primaryKey().bindTo { it.id }
    var url = varchar("url").bindTo { it.url }
    // 服务响应头需要解析的字段列表
    var data = varchar("data").bindTo { it.data }
}