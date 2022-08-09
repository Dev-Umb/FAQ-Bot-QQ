@file:Suppress(
    "EXPERIMENTAL_API_USAGE",
    "DEPRECATION_ERROR",
    "OverridingDeprecatedMember",
    "INVISIBLE_REFERENCE",
    "INVISIBLE_MEMBER"
)
package ink.umb.faqbot.dto.db

import org.apache.logging.log4j.LogManager.getLogger
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.util.StackLocatorUtil

fun logger(): Logger {
    System.setProperty("nacos.logging.default.config.enabled", "false");
    return getLogger(StackLocatorUtil.getStackTraceElement(2).className)
}