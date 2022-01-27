## 4. response
> 클라이언트 응답방법을 알아본다.  

응답하는 방법은 
- call.respondHtml()
- call.respondText()
- call.respond()

위의 3가지 메소드를 사용하는 방법이 일반적이다.

1. text 또는 html

단순히 문자열을 응답하는 방법으로는 call.respondText()을 사용한다.

~~~kotlin
call.respondText("hello world", contentType = ContentType.Text.Plain)
~~~   

그리고 간단한 HTML 출력은 call.respondHtml()을 호출하여 DSL(Domain-Specific Languages)로 작성된 HTML 스크립트로 값을 전달할 수 있다. 

~~~kotlin
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
~~~

2. object
json 형태의 클래스나 각종 템플릿(mustache, FreeMaker)을 변환하여 text로 응답하려면 call.respond()를 호출한다.   


~~~kotlin
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
~~~

3. file 
   
call.response.header()를 통해 해더를 설정 후, call.respondFile()을 통해 파일을 전송한다.   
~~~kotlin
fun Application.main() {
    routing {
        get("/download") {
            val file = File("files/ktor_logo.png")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(
                   ContentDisposition.Parameters.FileName, "ktor_logo.png")
                    .toString()
            )
            call.respondFile(file)
        }
    }
}
~~~
5. etc 
   - header
   - cookies
   - content type, status
   - redirect
   

   
