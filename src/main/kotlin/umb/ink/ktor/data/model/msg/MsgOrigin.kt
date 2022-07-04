package umb.ink.ktor.data.model.msg

import androidx.annotation.IntDef
import umb.ink.ktor.data.model.msg.MsgOrigin.Companion.FRIEND
import umb.ink.ktor.data.model.msg.MsgOrigin.Companion.GROUP

@IntDef(GROUP,FRIEND)
annotation class MsgOrigin{
    companion object{
        const val GROUP: Int = 0
        const val FRIEND: Int = 1
    }
}
