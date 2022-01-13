#### 1. QuickStart
> 빠르게 시작하기 

1. 플러그인 설치
2. 빌드 및 배포

(1) 플러그인들을 빌드해서 배포한다.
~~~
./gradlew installDist
~~~
![](./images/build_1.jpg)
(2) 생성된 바이너리들이 build/install/ 하위 폴더에 생성된다.
![](./images/build_2.jpg)

(3) 실행바이너리 또는 스크립트를 실행한다. 윈도우의 경우, 2개 모두 생성된다.



[참고사이트](https://ktor.io/docs/gradle-application-plugin.html#package)

   

3. 로그처리 

서버개발에 있어서 Log관리는 무척중요한 부분이다. 다중 접속되는 클라이언트의 상황을 분석하다보면 단순히 메시지만 처리하는 것으로 끝나지 않고 데이터화하여 파일로 관리되어야 할 때가 많기 때문이다.


[참고사이트](https://ktor.io/docs/logging.html#configure-logback)