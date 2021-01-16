package io.farewell12345.github.faqbot.DTO.model.Games

import io.farewell12345.github.faqbot.DTO.DB.DB
import me.liuwj.ktorm.dsl.*

object GameDBUntil {
    private val GameDB = DB.gameDB
    private fun getGame(gameName: String,group:Long): QueryRowSet {
        return GameDB.from(Game).select().where {
            (Game.name eq gameName) and (Game.group eq group)
        }.rowSet
    }
    private fun getMember(qq:Long): QueryRowSet {
        return GameDB.from(User).select().where {
            User.qq eq qq
        }.rowSet
    }
    private fun getRemote(gameId:Int,userId:Int): QueryRowSet {
        return GameDB.from(GameAndUserRemote).select().where {
            (GameAndUserRemote.gameId eq gameId) and
                    (GameAndUserRemote.userId eq userId)
        }.rowSet
    }
    fun deleteMember(qq:Long){
        val user = getMember(qq)
        if (!user.next())
            return
        GameDB.delete(GameAndUserRemote){
            it.userId eq user[User.id] as Int
        }
        GameDB.delete(User){
            it.id eq user[User.id] as Int
        }
    }
    fun rollbackGame(gameName: String,qq: Long,group: Long):Boolean{
        val user = getMember(qq)
        val game = getGame(gameName,group)
        if (!user.next()|| !game.next()){
            return false
        }
        GameDB.delete(GameAndUserRemote){
            (it.userId eq user[User.id] as Int) and
                    (it.gameId eq game[Game.id] as Int)
        }
        return true
    }
    fun deleteGame(gameName: String,group: Long): Boolean {
        val game = getGame(gameName,group)
        if (!game.next()){
            return false
        }
        GameDB.delete(GameAndUserRemote){
            (it.gameId eq game[Game.id] as Int)
        }
        GameDB.delete(Game){
            (it.id eq game[Game.id] as Int) and (it.group eq group)
        }
        return true
    }
    fun getGroupGamesRowSet(group: Long): QueryRowSet {
        return GameDB.from(Game).select().where {
            Game.group eq group
        }.rowSet
    }
    fun getGameMember(gameName: String, group: Long): QueryRowSet {
        val remote = GameDB.from(GameAndUserRemote)
            .leftJoin(User,on = User.id eq GameAndUserRemote.userId)
            .rightJoin(Game,on = Game.id eq GameAndUserRemote.gameId)
            .select()
            .where {
                (Game.name eq gameName) and (Game.group eq group)
            }.rowSet
        return remote
    }
    fun addMemberToGame(gameName: String,group: Long,member:Long): Boolean {
        val game = getGame(gameName, group)
        val user = getMember(member)
        val gameId = if (!game.next()) GameDB.insertAndGenerateKey(Game){
            set(Game.name,gameName)
            set(Game.group,group)
        } else game[Game.id]
        val userId = if (!user.next()) GameDB.insertAndGenerateKey(User){
            set(User.qq,member)
        }else user[User.id]
        if (getRemote(gameId as Int, userId as Int).next()) return false
        GameDB.insert(GameAndUserRemote){
            set(GameAndUserRemote.gameId,gameId)
            set(GameAndUserRemote.userId,userId)
        }
        return true
    }

}

fun main() {
    GameDBUntil.getGroupGamesRowSet(114514).query.forEach {
        println(it[Game.name])
    }
    GameDBUntil.getGameMember("大鸟转转转",114514).query.forEach {
        println(it[User.qq])
    }
    GameDBUntil.deleteGame("转转转",114514)
    GameDBUntil.deleteMember(2333)
    GameDBUntil.getGroupGamesRowSet(114514).query.forEach {
        println(it[Game.name])
    }
    GameDBUntil.getGameMember("大鸟转转转",114514).query.forEach {
        println(it[User.qq])
    }
}