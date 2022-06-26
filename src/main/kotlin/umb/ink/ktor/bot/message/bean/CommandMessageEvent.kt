package umb.ink.ktor.bot.message.bean

import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent

fun GroupMessageEvent.isCommandEvent() = sender.permission.isOwner()