package io.farewell12345.github.faqbot.DTO.DB

import io.farewell12345.github.faqbot.AppConfig
import me.liuwj.ktorm.database.Database

object DB{
    private val appConfig= AppConfig.getInstance()

    val database=Database.connect(
        url= appConfig.dbUrl,
        user = appConfig.dbUser,
        password = appConfig.dbPwd
    )
}

