package umb.ink.ktor.routing.until

import androidx.annotation.IntDef
import umb.ink.ktor.routing.until.CommandPermissions.Companion.BLACK_USER
import umb.ink.ktor.routing.until.CommandPermissions.Companion.MANAGER
import umb.ink.ktor.routing.until.CommandPermissions.Companion.MEMBER
import umb.ink.ktor.routing.until.CommandPermissions.Companion.ROOT

@IntDef(ROOT,MANAGER,MEMBER,BLACK_USER)
annotation class CommandPermissions {
    companion object{
        const val ROOT = 3
        const val MANAGER = 2
        const val MEMBER = 1
        const val BLACK_USER = 0
    }
}