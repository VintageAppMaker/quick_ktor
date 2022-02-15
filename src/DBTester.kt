package com.psw

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.asIterable
import me.liuwj.ktorm.dsl.from
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.select
import java.util.*

fun testDb(){
    val database = Database.connect("jdbc:mysql://localhost:3306/ktorm", user = "root", password = "root")
    testDSL(database)
    testNativeSQL(database)
}

private fun testNativeSQL(database: Database) {
    // native sql
    val names = database.useConnection { conn ->
        val sql = """
        select name, password from t_user
        where name like ?
        order by id
    """

        conn.prepareStatement(sql).use { statement ->
            statement.setString(1, "user%")
            statement.executeQuery().asIterable().map {
                listOf<String>(it.getString(1), it.getString(2))
            }
        }
    }
    names.forEach { println("native : ${it[0]} : ${it[1]}") }
}

private fun testDSL(database: Database) {
    // 값 입력(DSL)
    database.insert(UserDB) {
        set(it.name, "user@${Calendar.getInstance().timeInMillis}".run { substring(0, length - 1) })
        set(it.password, "${Calendar.getInstance().timeInMillis}.pswd".run { substring(0, length - 1) })
    }

    for (row in database.from(UserDB).select()) {
        println("name => ${row[UserDB.name]} password => ${row[UserDB.password]} ")
    }

}

fun main() {
    testDb()
}