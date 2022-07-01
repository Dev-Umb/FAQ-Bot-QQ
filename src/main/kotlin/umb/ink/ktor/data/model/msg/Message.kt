package umb.ink.ktor.data.model.msg

import org.ktorm.schema.Column


interface Message {
    var id: Column<Int>
    var data: Column<String>
    /** 发送者 */
    var sender: Column<Long>

    /** 群号（可空） */
    var group: Column<Long>?
}