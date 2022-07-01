package umb.ink.ktor.data.model.msg

import org.ktorm.schema.*

class WelcomeMessage() : Table<Nothing>("welcome"), Message {
    override var id: Column<Int> = int("id").primaryKey()
    override var group: Column<Long>? = long("from")
    override var sender: Column<Long> = long("sender")
    override var data: Column<String> = varchar("talk")
}