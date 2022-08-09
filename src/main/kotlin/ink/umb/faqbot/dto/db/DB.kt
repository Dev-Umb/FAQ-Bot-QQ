package ink.umb.faqbot.dto.db

import ink.umb.faqbot.AppConfig
import me.liuwj.ktorm.database.Database

object DB{
    private val appConfig= AppConfig.getInstance()

    val database = Database.connect(
        url= appConfig.dbUrl,
        user = appConfig.dbUser,
        password = appConfig.dbPwd
    )
}

