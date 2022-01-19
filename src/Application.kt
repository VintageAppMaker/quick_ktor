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
    }
}

private fun Routing.gsonRoute() {
    get("/json/gson") {
        //call.respond(mapOf("hello" to "world"))

        call.respond(User(1, "test"))
    }
}

private fun Routing.mustacheRoute() {
    get("/html-mustache") {
        call.respond(MustacheContent("index.hbs", mapOf("user" to MustacheUser(1, "user1"))))
    }
}

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

private fun Routing.logRoute() {
    get("/") {
        call.application.environment.log.info("Hot WORLD!")
        call.respondText("Hot WORLD!", contentType = ContentType.Text.Plain)
    }
}

data class MustacheUser(val id: Int, val name: String)
data class User(val id: Int, val name: String)


