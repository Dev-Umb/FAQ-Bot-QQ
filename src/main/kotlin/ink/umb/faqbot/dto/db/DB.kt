package ink.umb.faqbot.dto.db

import ink.umb.faqbot.AppConfig
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.support.sqlite.SQLiteDialect

object DB{
    private const val dbName = "faq.db"
    val database = Database.connect(
        url = "jdbc:sqlite:$dbName",
        dialect = SQLiteDialect()
    )
}

