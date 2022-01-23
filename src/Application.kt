package com.psw

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.mustache.Mustache
import io.ktor.mustache.MustacheContent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates/mustache")
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    install(CallLogging) {
        filter { call ->
            call.request.path().startsWith("/html-dsl")
        }
    }

    routing {
        logRoute()
        dslRoute()
        mustacheRoute()
        gsonRoute()
        parameterRoute()
    }
}

// gson example
private fun Routing.gsonRoute() {
    get("/json/gson") {
        //call.respond(mapOf("hello" to "world"))

        call.respond(User(1, "test"))
    }
}

// mustache example
private fun Routing.mustacheRoute() {
    get("/html-mustache") {
        call.respond(MustacheContent("index.hbs", mapOf("user" to MustacheUser(1, "user1"))))
    }
}

// html dsl example
private fun Routing.dslRoute() {
    get("/html-dsl") {
        call.respondHtml {
            body {
                h1 { +"HTML" }
                ul {
                    for (n in 1..10) {
                        li { +"$n" }
                    }
                }
            }
        }
    }
}

// log 출력 example
private fun Routing.logRoute() {
    get("/") {
        val path    = call.request.uri
        val path2   = call.request.path()
        val resText = "Hot WORLD! uri=${path} path()=${path2} headers = ${call.request.headers.names()} headers[\"User-Agent\"]} = ${call.request.headers["User-Agent"]}"
        call.application.environment.log.info(resText)
        call.respondText(resText, contentType = ContentType.Text.Plain)

    }
}

// parameter example
private fun Routing.parameterRoute() {
    get("/param/{action}") {

        if(call.parameters["action"] == "add"){
            val value1  =  call.request.queryParameters["value1"]
            val sum     = 10  +   if ( value1 is String) value1.trim().toInt() else 0
            val resText = "10 + ${value1}(value1) = ${sum}"
            call.respondText(resText, contentType = ContentType.Text.Plain)
        }

    }

    post("/post/addObject"){
        val addUser = call.receive<User>()
        userList.add(addUser)
        var info = ""
        userList.forEach { info += "${it.id} : ${it.name} \n" }
        call.respondText("addUser => ${addUser.id} \n ${info}", status = HttpStatusCode.Created)
    }
}


// example data
val  userList : MutableList<User> = mutableListOf()
data class MustacheUser(val id: Int, val name: String)
data class User(val id: Int, val name: String)


