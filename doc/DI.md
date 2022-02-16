## 8. DI(Dependancy Injection)
> 서버 프로그래밍(Java)에서 관용적으로 사용하는 객체주입 방법(kodein)  

1. gradle 설정
~~~
    implementation "org.kodein.di:kodein-di-framework-ktor-server-jvm:7.0.0"
~~~

2. kodein 공식문서 

    - [공식문서](https://docs.kodein.org/kodein-di/7.10/getting-started.html)
   

- (a) plugin 설치 및 바인딩 
~~~kotlin
di {
    bindServices()
}
~~~

- (b) Injection 
~~~kotlin
fun DI.MainBuilder.bindServices(){
    bind<DBService>() with singleton { DBService() }
}
~~~

- (c) Using
~~~kotlin
// DB * connection example
private fun Routing.dbRoute() {
    val dbServ by di().instance<DBService>()
    get("/db/hikari") {
        call.respond(dbServ.getAll())
    }
}
~~~
   