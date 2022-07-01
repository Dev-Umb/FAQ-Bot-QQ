package umb.ink.ktor.data.bind

import org.ktorm.entity.Entity

interface BaseBind<T: BaseBind<T>>: Entity<T> {
    val id:Int
}