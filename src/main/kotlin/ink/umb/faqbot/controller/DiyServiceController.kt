package ink.umb.faqbot.controller

import ink.umb.faqbot.dto.db.DB
import ink.umb.faqbot.dto.model.DiyService
import ink.umb.faqbot.dto.model.DiyService.service
import ink.umb.faqbot.http.FuckOkhttp
import io.farewell12345.github.faqbot.dto.model.QA.DiyServiceBind
import io.farewell12345.github.faqbot.dto.model.QA.MatchMode
import io.farewell12345.github.faqbot.dto.model.QA.RequestMethod
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.forEach
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.toList
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.findIsInstance

object DiyServiceController {

    fun addService(pattern: String,  url: String, patternMode: @MatchMode Int, requestMethod: @RequestMethod Int): Int {
        var service = findService(pattern)
        if (service != null){
            return false as Int
        }
        service = DiyServiceBind{
            this.pattern = pattern
            this.url = url
            this.requestMethod = requestMethod
            this.patternMode = patternMode
        }

        return DB.database.service.add(service)
    }
    private fun findService(pattern: String): DiyServiceBind? {
        return try {
            DB.database.service.filter {
                it.parttern eq pattern
            }.toList()[0]
        }catch (e: IndexOutOfBoundsException){
            null
        }

    }

    private fun findService(id: Int): DiyServiceBind{
        return DB.database.service.filter {
            it.id eq id
        } .toList()[0]
    }
    private fun deleteService(id: Int){
        findService(id).delete()
    }

    fun getAllService(): List<String> {
        return buildList {
            DB.database.from(DiyService).select().forEach {
                add("${it[DiyService.id]}: ${it[DiyService.parttern]}\n")
            }
        }
    }
    private fun argsBody(args: List<String>): Map<String, String>{
        return buildMap {
            "args" to args.joinToString()
        }
    }
    fun getServiceResponse(msg: GroupMessageEvent): String? {
        val args: List<String> = msg.message.findIsInstance<PlainText>()?.content.toString().split(" ")
        if (args.isEmpty()) throw NoArgsException()
        val pattern = args[0]
        val service = findService(pattern)
//        when(service.patternMode){
//            MatchMode.precise -> {
//
//            }
//        }

        return when(service?.requestMethod){
           RequestMethod.post -> FuckOkhttp(service.url).post(argsBody(args))
            else -> {
                FuckOkhttp(service?.url).get()
            }
        }
    }
    class NoArgsException: Throwable(){

    }
    class NoServiceException: Throwable() {

    }
}