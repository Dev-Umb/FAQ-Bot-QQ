@file:Suppress(
        "EXPERIMENTAL_API_USAGE",
        "DEPRECATION_ERROR",
        "OverridingDeprecatedMember",
        "INVISIBLE_REFERENCE",
        "INVISIBLE_MEMBER"
)

package ink.umb.faqbot

import ink.umb.faqbot.dto.db.DB
import ink.umb.faqbot.plugin.ProgramManage


suspend fun main() {
    // 添加监听job
    try {
        DB
        ProgramManage.run()
    }catch (_: Exception){
        ProgramManage.restart()
    }
}