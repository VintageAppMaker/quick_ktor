package com.psw.db

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.asIterable
import me.liuwj.ktorm.dsl.*
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
        set(UserDB.name, "user@${Calendar.getInstance().timeInMillis}".run { substring(0, length - 1) })
        set(UserDB.password, "${Calendar.getInstance().timeInMillis}.pswd".run { substring(0, length - 1) })
    }

    // select 예제 (1)
    for (row in database.from(UserDB).select()) {
        println("DSL #1 :name => ${row[UserDB.name]} password => ${row[UserDB.password]} ")
    }

    // select 예제 (2)
    data class selectData(val name: String?, val passwd: String?)
    val results = database
        .from(UserDB)
        .select()
        .where { ( UserDB.name like "%jerry%") }
        .orderBy(UserDB.name.asc())
        .map { row ->
            selectData(
                name =   row[UserDB.name],
                passwd = row[UserDB.password]
            )
        }

    results.forEach {
        println("DSL #2 :name => ${it.name} password => ${it.passwd} ")
    }

}

fun main() {
    testDb()
}