## 8. DI(Dependency Injection)
> Kodein을 이용한 의존성 주입  

1. gradle 설정

- [mvnrepository.com에서 찾기 ](https://mvnrepository.com/artifact/org.kodein.di/kodein-di-framework-ktor-server-jvm/7.0.0)

kotlin 버전이 맞지않을 경우가 발생할 수 있으니, 기존 설정과 충돌되지 않는 버전을 선택하여 설정한다.  

~~~
implementation("org.kodein.di:kodein-di-jvm:7.0.0")
implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:7.0.0")
~~~

2. 초기설정

    - [공식문서](https://kodein.org/Kodein-DI/?6.3/ktor)
   
