##2. Start
> 필수모듈 이해하기  

1. 진입점

프로그램이 시작되는 main 함수는 아래와 같이 main이다.

~~~kotlin
fun main() {
    ...
}
~~~   
그리고 main 함수 안에서 embeddedServer()를 실행하여 서버환경 및 플러그인 설치, 웹주소 라우팅을 구현한다. 
~~~kotlin
fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
        }
    }.start(wait = true)
}
~~~   

embeddedServer의 첫번째 파라메터는 서버엔진이다. 서버엔진은
- netty
- jetty
- tomcat
- cio

등으로 설정할 수 있다. 두번째 파라메터는 통신 port이다. 세번째 파라메터({}로 정의된 부분)는 람다식 함수로 embeddedServer를 실행하며 초기화 하는 내용을 담고 있다.
주로 플러그인 설치(install{})와 웹주소 라우팅(routing{})을 정의한다.  

그리고 main 함수에 EngineMain을 대입하고 실제 main 함수는 Application의 확장함수로 대체(주로 module)하는 경우도 있다 .

~~~kotlin
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
~~~

위와 같이 했을 경우는 /resources/application.conf에서 확장함수 정보를 다음과 같이 정의해야 한다.

~~~
ktor {
    deployment {
        port = 8080
        port = ${?PORT}
    }
    application {
        modules = [ com.psw.ApplicationKt.module ]
    }
}

~~~

application의 modules에 확장함수의 패키지명을 입력한다. 
그리고 다음과 같이 확장함수를 정의하면 ktor 서버가 실행시 main 함수 호출 후, 정의된 확장함수를 실행시킨다.

~~~kotlin
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }
}
~~~


2. install, routing
