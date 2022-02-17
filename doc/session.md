## 9. Session
> ktor에서 session 다루기  

1. gradle 설정
~~~
    implementation "io.ktor:ktor-server-sessions:$ktor_version"
~~~

2. 공식문서 

    - [공식문서](https://ktor.io/docs/sessions.html)
   

- (a) plugin 설치
세션에서 사용할 클래스를 정의한다(UserSession). 
~~~kotlin
install(Sessions) {
   cookie<UserSession>("user_session")
}
~~~

- (b) using
  
call.sessions.set()으로 값을 저장한다. 
세션값 가져오기는 call.sessions.get<데이터형>()을 이용한다. 
가져온 값을 수정하려면 바로 값을 대입할 수 없다. copy() 메소드를 사용하여 수정한다. 
그리고 세션값을 지우려면 call.sessions.clear<데이터형>()을 이용한다. 

~~~kotlin
private fun Routing.sessionRoute() {
   get("/session/set") {
      call.sessions.set(UserSession(id = "123abc", count = 0))
      call.respondRedirect("/session")
   }

   get("/session") {
      var userSession = call.sessions.get<UserSession>()
      call.sessions.set(userSession!!.copy(count = userSession.count + 1))

      call.respondText ("${userSession!!.count}")
   }

   get("/session/clear") {
      call.sessions.clear<UserSession>()
      call.respondRedirect("/")
   }
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
   