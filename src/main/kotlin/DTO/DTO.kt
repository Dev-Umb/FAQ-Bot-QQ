package DTO

import com.google.gson.Gson
import me.liuwj.ktorm.schema.*
import java.util.*
import kotlin.collections.ArrayList

object Question: Table<Nothing>("question") {
    val id = int("id").primaryKey()
    val question = varchar("question")
    val group=long("group_id")

    val answer=varchar("answer")

    val lastEditUser=long("last_edit_user")
}
data class Answer (
    val imgList : LinkedList<String>,
    val atList :LinkedList<Long>,
    val text : String
)