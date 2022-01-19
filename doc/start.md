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

[참고링크](https://ktor.io/docs/routing-in-ktor.html#define_route) 를 요약함 

- install 사용

  install 함수를 사용하면 원하는 객체를 사용할 수 있다. 첫번째 파라메터는 ApplicationFeature형의 클래스이다. 
  두번째 파라메터는 초기화 및 환경설정을 실행하는 클로져(configuration)로 필요에 기능을 구현하기도 비어놓은 상태로 사용하기도 한다.   
  
~~~kotlin
install(Routing) {
  // ...
}
~~~

- Routing 사용
  
  routing() 함수는 url을 분기처리하는 함수이다.
  주로 진입점이 되는 함수에서 사용되고 Application 확장함수이다.
  크게 2가지 방법으로 사용된다. 첫 번째는
~~~kotlin
import io.ktor.routing.*
install(Routing) {
    
}
~~~
과 같은 방법이 있다. 그러나 더 간편하게 클로져 형태로 사용할 수도 있다.
~~~kotlin
routing {

}
~~~

- Routing 핸들러 정의 

  routing{} 안에서 route() 함수를 사용한다. 
  첫번째 파라메터는 uri이고 두번째 파라메터는 Http 요청의 종류이다. 
  
~~~kotlin
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.response.*

routing {
    route("/hello", HttpMethod.Get) {
        handle {
            call.respondText("Hello")
        }
    }
}
~~~

위의 방법보다 더 간소화된 방법은 다음과 같다. 

~~~kotlin
import io.ktor.routing.*
import io.ktor.response.*

routing {
    get("/hello") {
        call.respondText("Hello")
    }
}
~~~
route() 함수대신에 http 요청에 따라 get(), post(), put(), delete() 함수를 사용하며 파라메터는 요청 uri이다. 


- 패턴
  - 변수치환
    
    {변수}로 되어있는 경우는 핸들러 안에서 변수로 사용가능하다. 예로 /user/{name}이라면 name 변수를 사용가능하다.
  - "*"를 이용한 패턴
    
    "*" 를 이용한 패턴은 /다음의 어떤 문자열이 와도 매칭이 되는 경우이다. /user/*는 /user/add와 매칭이 된다. 단, null 인경우(/user/)는 매칭되지 않는다.
  - "{...}"를 이용한 패턴 

    "{...}" 를 이용한 패턴은 /다음의 하부주소까지 패턴을 적용한다. /user/{...}는 /user/info/setting과 매칭이 된다. 

그리고 라우팅되는 파라메터는 call 객체의 parameters를 이용하여 액세스 가능하다. 
~~~kotlin
get("/user/{login}") {
    if (call.parameters["login"] == "admin") {
        // ...
    }
}
~~~

