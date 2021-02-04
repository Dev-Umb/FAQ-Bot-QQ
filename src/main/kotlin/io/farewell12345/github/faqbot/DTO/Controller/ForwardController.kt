package io.farewell12345.github.faqbot.DTO.Controller

import io.farewell12345.github.faqbot.DTO.DB.DB
import io.farewell12345.github.faqbot.DTO.model.QAmodel.Group
import me.liuwj.ktorm.dsl.*

object ForwardController {
    @ExperimentalStdlibApi
    fun getForwardList(user:Long): List<Long> {
        try {
            return buildList<Long> {
                DB.database.from(Group).select().where {
                    (Group.user_id eq user)
                }.forEach {
                    add(it[Group.groupID] as Long)
                }
            }
        }catch (e:Exception){
            throw e
        }
    }

    fun addForwardGroup(user: Long,group:Long){
        try {
            if (DB.database.from(Group).select().where {
                    (Group.user_id eq user) and (Group.groupID eq group)
                }.rowSet.size() > 0){
                return
            }
            DB.database.insert(Group) {
                set(Group.groupID, group)
                set(Group.user_id, user)
            }
        }catch (e:Exception){
            throw e
        }
    }

    fun deleteForwardGroup(user:Long,group: Long){
        try{
            DB.database.delete(Group){
                (it.user_id eq user) and (it.groupID eq group)
            }
        }catch (e:Exception){
            throw e
        }
    }
}