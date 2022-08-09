package ink.umb.faqbot.dto.model

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long

object Group: Table<Nothing>("group") {
    val groupID = long("group")
    val user_id = long("user_id")
}