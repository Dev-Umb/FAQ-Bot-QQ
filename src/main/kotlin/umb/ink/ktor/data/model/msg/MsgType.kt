package umb.ink.ktor.data.model.msg

import androidx.annotation.IntDef
import umb.ink.ktor.data.model.msg.MsgType.Companion.APPLETS_MESSAGE
import umb.ink.ktor.data.model.msg.MsgType.Companion.FORWARD_MESSAGE
import umb.ink.ktor.data.model.msg.MsgType.Companion.IMAGE_MESSAGE
import umb.ink.ktor.data.model.msg.MsgType.Companion.IMAGE_MESSAGE_AND_TEXT_LIST
import umb.ink.ktor.data.model.msg.MsgType.Companion.IMAGE_MESSAGE_LIST
import umb.ink.ktor.data.model.msg.MsgType.Companion.TEXT_MESSAGE
import umb.ink.ktor.data.model.msg.MsgType.Companion.XML_MESSAGE

@IntDef(
    TEXT_MESSAGE,
    IMAGE_MESSAGE,
    IMAGE_MESSAGE_LIST,
    IMAGE_MESSAGE_AND_TEXT_LIST,
    XML_MESSAGE,
    FORWARD_MESSAGE,
    APPLETS_MESSAGE
)
annotation class MsgType{
    companion object{
        const val TEXT_MESSAGE = 0
        const val IMAGE_MESSAGE = 1
        const val IMAGE_MESSAGE_LIST = 2
        const val IMAGE_MESSAGE_AND_TEXT_LIST = 3
        const val XML_MESSAGE = 4
        const val FORWARD_MESSAGE = 5
        const val APPLETS_MESSAGE = 6
    }
}