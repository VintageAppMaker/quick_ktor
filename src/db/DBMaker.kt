package com.psw.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.asIterable

// 데이터베이스
var database : Database? =null

// hikari를 통한 DB 환경설정
fun initDB() {

    fun hikariConfig(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "com.mysql.cj.jdbc.Driver"
        config.jdbcUrl = "jdbc:mysql://localhost:3306/ktorm"

        config.username ="root"
        config.password ="root"

        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    database = Database.connect(hikariConfig())
}

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