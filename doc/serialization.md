## 5. 직렬화
> json 클래스를 직렬화하여 사용하기   

직렬화를 사용하면 data class를 json 형태로 쉽게 사용할 수 있다. 다양한 모듈이 있지만 gson이 가장 편리하다. gradle에서 설정은 다음과 같다.
~~~
implementation "io.ktor:ktor-gson:$ktor_version"
~~~

~~~kotlin
install(ContentNegotiation) {
    gson {}
}
~~~

~~~kotlin

// gson example
private fun Routing.gsonRoute() {
    get("/json/gson") {
        call.respond(User(1, "test"))
    }
}

...

data class User(val id: Int, val name: String)

~~~