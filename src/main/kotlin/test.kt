import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.logging.ConsoleLogger
import me.liuwj.ktorm.logging.LogLevel
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

fun main() {
    val sqlName="fqa"
    val DBUserName="root"
    val DBPassword="root"
    val DBUrl="jdbc:mysql://localhost:3306/${sqlName}"
    val conn= DriverManager.getConnection(DBUrl, DBUserName, DBPassword);
//    val database=DSL.using(conn, SQLDialect.MARIADB)
//    val restult=database.select().from(Question).fetchOne().into()
//    print(restult)
    val database=Database.connect(
            url = DBUrl,
            user = DBUserName,
            password = DBPassword
    )
    for(row in database.from(Question).select()){
        println(row.query.database.name)
    }
}