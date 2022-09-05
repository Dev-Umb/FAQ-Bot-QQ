package ink.umb.faqbot.dto.model

import ink.umb.faqbot.dto.model.Group.bindTo
import io.farewell12345.github.faqbot.dto.model.QA.DiyServiceBind
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

object DiyService: Table<DiyServiceBind>("service") {
    val Database.service get() = sequenceOf(DiyService)
    var id = int("id").primaryKey().bindTo { it.id }
    // 匹配命令
    var parttern = varchar("pattern").bindTo { it.pattern }
    var patternMode = int("pattern_mode").bindTo { it.patternMode }
    var requestMethod = int("req_method").bindTo { it.requestMethod }
    var url = varchar("url").bindTo { it.url }
    val contentType = varchar("content_type").bindTo { it.contentType  }
//    // 服务响应头需要解析的字段列表
//    var data = varchar("data").bindTo { it.data }
}