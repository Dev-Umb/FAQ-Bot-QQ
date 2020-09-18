import me.liuwj.ktorm.schema.*

object Question: Table<Nothing>("question") {
    val id = int("id").primaryKey()
    val question = varchar("question")
    val group=long("group")
    val answer =text("answer")
    val lastEditUser=long("lastedituser")
}