package io.farewell12345.github.faqbot.dto.model.QA

import me.liuwj.ktorm.entity.Entity
@Target(AnnotationTarget.TYPE)
annotation class MatchMode{
    companion object{
        // 模糊匹配
        const val obscure: Int = 0
        // 精确匹配
        const val precise: Int = 1
        // 无参数匹配
        const val noArgs: Int = 2
    }
}
@Target(AnnotationTarget.TYPE)
annotation class RequestMethod{
    companion object{
        // Post
        const val post: Int = 0
        // Get
        const val get: Int = 1
    }
}
interface DiyServiceBind: Entity<DiyServiceBind> {


    companion object : Entity.Factory<DiyServiceBind>()
    var id:Int
    var pattern: String
    var patternMode: @MatchMode Int
    var requestMethod: @RequestMethod Int
    var url: String
//    // 服务响应头需要解析的字段列表
//    var data: String
}