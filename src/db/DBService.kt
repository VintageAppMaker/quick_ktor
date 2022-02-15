package com.psw.db

import com.psw.database
import me.liuwj.ktorm.database.asIterable

// db Service
class DBService{
    fun getAll() : List<AccountInfo>{
        val lst : MutableList<AccountInfo> = mutableListOf()

        val names = database!!.useConnection { conn ->
            val sql = """
            select id, name, password from t_user
            order by id
        """
            conn.prepareStatement(sql).use { statement ->
                statement.executeQuery().asIterable().map {
                    listOf(it.getString(1), it.getString(2))
                }
            }
        }
        names.forEach { lst.add(AccountInfo(it[0], it[1])) }
        return lst.toList()
    }
}