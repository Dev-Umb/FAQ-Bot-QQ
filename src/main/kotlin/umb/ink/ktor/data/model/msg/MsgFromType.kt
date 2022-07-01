package umb.ink.ktor.data.model.msg

import androidx.annotation.IntDef
import umb.ink.ktor.data.model.msg.MsgFromType.Companion.FRIEND
import umb.ink.ktor.data.model.msg.MsgFromType.Companion.GROUP

@IntDef(GROUP,FRIEND)
annotation class MsgFromType{
    companion object{
        const val GROUP: Int = 0
        const val FRIEND: Int = 1
    }
}
