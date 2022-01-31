package com.psw

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.schema.*
import java.util.*

// 결과에 필요한 필드만 정의
object UserDB : Table<Nothing>("t_user") {
    //val id = int("id").primaryKey()
    val name = varchar("name")
    val password = varchar("password")
}

fun testDb(){
    val database = Database.connect("jdbc:mysql://localhost:3306/ktorm", driver = "org.h2.Driver", user = "root", password = "root")

    // 값 입력
    database.insert(UserDB) {
        set(it.name, "user@${Calendar.getInstance().timeInMillis}".run { substring(0, length - 1) })
        set(it.password, "${Calendar.getInstance().timeInMillis}.pswd".run { substring(0, length - 1) })
    }

    for (row in database.from(UserDB).select()) {
        println("name => ${row[UserDB.name]} password => ${row[UserDB.password]} " )
    }
}

fun main() {
    testDb()
}