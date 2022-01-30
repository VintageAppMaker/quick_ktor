package com.psw

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.schema.*

object UserDB : Table<Nothing>("t_user") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val password = varchar("password")
}


fun testDb(){
    val database = Database.connect("jdbc:mysql://localhost:3306/ktorm", driver = "org.h2.Driver", user = "root", password = "root")

    for (row in database.from(UserDB).select()) {
        println(row[UserDB.name])
    }
}

fun main() {
    testDb()
}