package umb.ink.ktor.config

import org.ktorm.database.Database

object DBConfig {
    private annotation class DatabaseConfig {
        companion object{
            const val HOST = "bj-cynosdbmysql-grp-kf9vc2hk.sql.tencentcdb.com"
            const val PORT = "21437"
            const val NAME = "remake_qa"
            const val USER = "umb"
            const val PASSWORD ="fk!ttWL7:!w3tgn"
            fun getDBUrl():String{
                return "jdbc:mysql://$HOST:$PORT/$NAME"
            }
        }
    }
    private fun connect(): Database {
        return Database.connect(
            url = DatabaseConfig.getDBUrl(),
            user = DatabaseConfig.USER,
            password = DatabaseConfig.PASSWORD
        )
    }
    val INSTANCE:Database = connect()
}