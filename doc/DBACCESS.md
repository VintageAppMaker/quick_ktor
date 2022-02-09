## 7. database access
> DB를 다루는 방법  

1. gradle 설정
    - [mvnrepository.com에서 찾기 ](https://mvnrepository.com/artifact/org.ktorm/ktorm-core/3.3.0)

ktorm, mysql, h2 설정    
~~~
    implementation("com.h2database:h2:2.1.210")
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("me.liuwj.ktorm:ktorm-core:3.1.0")
~~~

2. ktorm

    - [공식문서](https://www.ktorm.org/)
   
    - DB 연결 
      
      Database.connect(url, user, password)
     ~~~kotlin
     val database = Database.connect("jdbc:mysql://localhost:3306/ktorm", user = "root", password = "root")
     ~~~
      
    - 테이블 정의
      
      object 클래스명 : Table<Nothing>("테이블명). 필요한 필드만 정의해도 된다. 
   
    ~~~kotlin
    // 필요한 필드만 정의해도 된다
    object UserDB : Table<Nothing>("t_user") {
       //val id = int("id").primaryKey()
       val name = varchar("name")
       val password = varchar("password")
    }
    ~~~   

   - Table 및 필드 액세스 

     database 객체의 
       - select( from, where, whereWithConditions, groupBy, having, orderBy, map, ...)
       - insert
       - update
       - delete
   
      메소드를 사용하여 sql문을 사용하듯 원하는 기능을 구현할 수 있다. 
   
   ~~~kotlin
   private fun testDSL(database: Database) {
       // 값 입력(DSL)
       database.insert(UserDB) {
           set(it.name, "user@${Calendar.getInstance().timeInMillis}".run { substring(0, length - 1) })
           set(it.password, "${Calendar.getInstance().timeInMillis}.pswd".run { substring(0, length - 1) })
       }

       for (row in database.from(UserDB).select()) {
           println("name => ${row[UserDB.name]} password => ${row[UserDB.password]} ")
       }
   }
   ~~~
   
   여러개 테이블 join 및 결과값 처리는 공식문서에서 다음과 같이 예를 제시하고 있다.
    ~~~ kotlin
    
    data class Names(val name: String?, val managerName: String?, val departmentName: String?)
    
    val emp = Employees.aliased("emp")
    val mgr = Employees.aliased("mgr")
    val dept = Departments.aliased("dept")
    
    val results = database
        .from(emp)
        .leftJoin(dept, on = emp.departmentId eq dept.id)
        .leftJoin(mgr, on = emp.managerId eq mgr.id)
        .select(emp.name, mgr.name, dept.name)
        .orderBy(emp.id.asc())
        .map { row ->
            Names(
                name = row[emp.name],
                managerName = row[mgr.name],
                departmentName = row[dept.name]
           )
        }
    
    ~~~

3. native sql 사용하기
   
database.useConnection { conn ->}을 사용하여 conn객체의 prepareStatement(sql문).use{}를 실행한다. 
그리고 넘겨진 statement에 setString(인덱스)를 통하여 파라메터 값을 넘기고 executeQuery().asIterable().map{}을 통해 나온 결과값을 가지고
row값의 인덱스를 getString(인덱스)하여 값을 가져온다.

~~~kotlin
private fun testNativeSQL(database: Database) {
    // native sql
    val names = database.useConnection { conn ->
        val sql = """
        select name, password from t_user
        where name like ?
        order by id
    """

        conn.prepareStatement(sql).use { statement ->
            statement.setString(1, "user%")
            statement.executeQuery().asIterable().map {
                listOf<String>(it.getString(1), it.getString(2))
            }
        }
    }
    names.forEach { println("native : ${it[0]} : ${it[1]}") }
}
~~~