package com.psw.db

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.varchar

// 필요한 필드만 정의해도 된다
object UserDB : Table<Nothing>("t_user") {
    //val id = int("id").primaryKey()
    val name = varchar("name")
    val password = varchar("password")
}

data class AccountInfo(var name : String, var passwd : String)
