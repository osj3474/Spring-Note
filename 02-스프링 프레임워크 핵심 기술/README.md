# 목차

1. [IoC 컨테이너](#1-ioc-컨테이너-)
<br /></t>
   1.1 [ApplicationContext](#1-1-applicationcontext-)
<br /></t>
   1.2 [Autowired](#1-2-autowired-)
<br /></t>
   1.3 [ComponentScan](#1-3-componentscan-)
<br /></t>
   1.4 [Bean 스코프](#1-4-bean-스코프-)
<br /></t>
   1.5 [Environment](#1-5-environment-)
<br /></t>
   1.6 [MessageSource](#1-6-messagesource-)
<br /></t>
   1.7 [ApplicationEventPublisher](#1-7-applicationeventpublisher-)
<br /></t>
   1.8 [ResourceLoader](#1-8-resourceloader-)
2. [Resource 추상화](#2-resource-추상화-)
3. [Validation 추상화](#3-Validation-추상화-)
4. [데이터 바인딩](#4-데이터-바인딩-)
5. [SpEL](#5-SpEL-)
6. [스프링 AOP](#6-스프링-AOP-)
7. [Null-Safety](#7-null-safety-)


<br /><br />

--------------------------------------------------------------------------

# 1. IoC 컨테이너 [👆](#목차)

## 정의

### 1) IoC 

 : 어떤 객체가 사용하는 의존 객체를 직접 만들어 사용하는게 아니라, 주입 받아 사용하는 방법이다.

쉽게 말하면,

```java
private BookRepository bookRepository;
```

이렇게만 선언하면 null일텐데, 스프링이 `bookRepository` 에 대한 정보를 주입을 해줘서 바로 사용을 할 수 있다.



<br />

### 2) IoC 컨테이너 

 : Bean들을 가지고 IoC 기능을 제공하는 컨테이너이다.

ex)

- BeanFactory : IoC컨테이너 최상위 인터페이스이다.
- ApplicationContext : BeanFactory를 상속받은 IoC컨테이너로, 가장 많이 사용한다.



<br />

### 3) Bean

 : **IoC 컨테이너에 의해 관리되는 객체**이다.

#### 장점

1. IoC컨테이너에 의해 **의존성 관리**가 된다.

2. IoC컨테이너에 등록된 Bean은 기본적으로 **싱글톤**으로 등록되어 싱글톤으로 사용하려고 할 때, 편하다.

   cf) 스코프

   - 싱글톤 : 하나만 만들어서 전체에 사용하는것
   - 프로토타입 : 매번 다른 객체

3. **라이프 사이클 인터페이스** 로 부가적인 작업을 할 수 있다.

   (Bean에 등록되면서 호출되는 메서드들을 이용하는 경우를 말한다.)



<br /><br />

--------------------------------------------------------------------------

# 1-1. ApplicationContext [👆](#목차)

## 1) (고전적인) Bean 등록 방법

: 스프링 IoC 컨테이너는 빈설정(xml) 파일이 있어야한다. 가령, `BookService` 가 `BookRepository` 를 주입받는다고 해보자.

```java
public class BookService{
   BookRepository bookRepository;

   public void setBookRepository(BookRepository bookRepository){
      this.bookRepository = bookRepository;
   }
}
```

이를 위해서는, 다음과 같이 일일이 Bean으로 등록을 해줘야 했다.

**application.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

  	<!-- 여기부터 -->
    <bean id="bookRepository"
        class="com.example.demo.BookRepository">
    </bean>
    
    <bean id="bookService"
        class="com.example.demo.BookService">
        <property name="bookRepository" ref="bookRepository" />
    </bean>
  	<!-- 여기까지 주목 -->
  
</beans>
```

<br />
이런 불편함을 해결하는 방법이 Component Scan으로 간편하게 빈을 등록하는 것이다.

**application.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

   <!-- 여기부터 -->
    <context:component-scan base-package="com.example.demo" />
   <!-- 여기까지 주목 -->
</beans>
```

위처럼 해주면,
 - Bean 등록 : @Component, @Service, @Repository, @Configuration+@Bean 조합 등
 - 의존성 주입 : @Autowired, 생성자, setter 등

cf) 참고로, @SpringBookApplication 어노테이션에 @ComponentScan이 있다.



<br /><br />

--------------------------------------------------------------------------

# 1-2. @Autowired [👆](#목차)

## **1) 사용할 수 있는 위치**
 - 생성자 (스프링 4.3부터는 생략 가능)
 - 세터
 - 필드

 cf) @Autowired(required = false) 라는 옵션을 주면, 의존성 주입 없이도 생성이 되게 할 수 있다.

<br />

## **2) Bean이 여러개인 경우, 처리 방법**

<br />

*BookRepository.java*

```java
public interface BookRepository{

}
```

*FirstBookRepository.java*
```java
@Repository
public class FirstBookRepository implements BookRepository{

}
```

*SecondBookRepository.java*

```java
@Repository
public class SecondBookRepository implements BookRepository{

}
```

<br />

이렇게 되면, BookRepository타입의 Bean이 2개가 생겼는데, 다음과 같이 BookRepository를 주입받으려고 하면, 다음의 상황에서 FirstBookRepository 와 SecondBookRepository 중에 어떤 Repository를 주입받을까?

<br />

*BookService.java*
```java
@Service
public class BookService{
   @Autowired
   BookRepository bookRepository;
}
```

<br />

## **2-1) 결과**

 : 에러가 난다.

<br />

## **2-2) 해결 방법**

**1. @Primary 이용** (추천)
<br />

 : Bean들 중 가장 우선순위를 높여서 다른 곳에서 주입하려고 할 때, @Primary 어노테이션이 있는 Bean을 주입받도록 한다.

 ```java
@Repository @Primary
public class FirstBookRepository implements BookRepository{

}
 ```
<br />

**2. @Qualifier 이용**
<br />

 : 어떤 Bean을 주입받을 지를 명시한다.

 ```java
@Service
public class BookService{
   @Autowired @Qualifier("FirstBookRepository")
   BookRepository bookRepository;
}
 ```

<br />

**3. 여러 Bean들 모두 주입**
<br />

 : List 타입으로 여러 Bean들을 모두 주입받도록 한다.

```java
@Service
public class BookService{
   @Autowired
   List<BookRepository> bookRepositories;  // FristBookRepository, SecondBookRepsitory
}
```

<br />

**4. 트릭** (비추천)
<br />

 : 필드명을 주입받고 싶은 Bean 이름으로 한다. (첫글자 소문자, 그 외 동일하게)

 ```java
@Service
public class BookService{
   @Autowired
   BookRepository firstBookRepository;
}
 ```


 <br /><br />

--------------------------------------------------------------------------

# 1-3. @ComponentScan [👆](#목차)

## **1) 스캔 동작**

 - @ComponentScan 어노테이션이 있는 곳부터 스캔이 시작된다. 
 - @SpringBootApplication이 @ComponentScan을 담고 있다.
 - @SpringBootApplication이 있는 패키지 안에 있는 것들만 스캔이 가능하다.
 - @Component 어노테이션이 있는 것들을 스캔한다.
 - @Filter로 스캔할 컴포넌트를 필터링할 수 있다.


<br />

## **2) 다른 패키지, Bean 등록**

: 다음은 다른 패키지에 있는 컴포넌트를 Bean에 등록하는 방법 중 하나이다.

```java
@SpringBootApplication
public class DemoApplication {

   @Autowired
   OtherPack otherPack;  // 다른 패키지라 intellij가 에러로 인식. (하지만, 실행 시 error없음.)

	public static void main(String[] args) {
		var app = new SpringApplication(DemoApplication.class);
		app.addInitializers(new ApplicationContextInitializer<GenericApplicationContext>() {
			@Override
			public void initialize(GenericApplicationContext ctx) {
				ctx.registerBean(OtherPack.class);   // 다른 패키지 클래스를 Bean 등록
			}
		});
		app.run(args);
	}
}
```


<br />

## **3) @Component**

: 아래의 어노테이션은 모두 @Component를 가지고 있다.
<br />
- @Repository
- @Service
- @Controller
- @Configuration
<br />


cf) 싱글톤 스코프인 Bean은 초기에 다 생성을 한다. (이런 Bean이 많으면, 초기 구동시간이 조금 걸릴 수 있다.)



<br /><br />

--------------------------------------------------------------------------

# 1-4. Bean 스코프 [👆](#목차)

## **1) 싱글톤**

 - 어플리케이션 전역에 걸쳐서 해당 Bean의 인스턴스가 하나이다.
 - 기본 스코프가 싱글톤이다. (사실 대부분의 경우, 싱글톤을 쓸 것임.)




<details>
   <summary> 예시 소스코드 ⭐️(누르기) </summary>
<br />

*Single.java*
```java
@Component
public class Single {
    @Autowired
    Proto proto;

    public Proto callProto(){
        return proto;
    }
}
```

*Proto.java*
```java
@Component
public class Proto {
}
```

*ScopeRunner.java*
``` java
@Component
public class ScopeRunner implements ApplicationRunner {

    @Autowired
    Single single;

    @Autowired
    Proto proto;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(proto);               // Proto@9cd25ff
        System.out.println(single.callProto());  // Proto@9cd25ff
    }
}
```

<br />

=> 결과를 보면, Runner가 주입받은 Proto라는 객체와, Single이 주입받은 Proto라는 객체가 완전히 일치함을 알 수 있다.
</details>




<br /><br />

## **2) 프로토타입**

 - 매번 새로운 인스턴스를 만들어서 사용하는 것. 
 - Bean을 받아올 때만, 새로운 인스턴스가 된다.



<details>
   <summary> 예시 소스코드 ⭐️(누르기) </summary>
<br />


*Proto.java*
 ```java
@Component @Scope("prototype")
public class Proto{

}
 ```


*ScopeRunner.java*
```java
@Component
public class ScopeRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Proto");
        System.out.println(ctx.getBean(Proto.class)); // Proto@780ec4a5
        System.out.println(ctx.getBean(Proto.class)); // Proto@e24ddd0
        System.out.println(ctx.getBean(Proto.class)); // Proto@6f70f32f

        System.out.println("Single");
        System.out.println(ctx.getBean(Single.class)); // Single@548e76f1
        System.out.println(ctx.getBean(Single.class)); // Single@548e76f1
        System.out.println(ctx.getBean(Single.class)); // Single@548e76f1
    }
}
```

<br />

=> 결과를 보면, Bean을 가지고 올때마다, 프로토 타입의 Bean의 경우, 새로운 인스턴스로 반환되는 것을 볼 수 있다.

</details>



<br /><br />

## **3) 프로토타입 주의 사항**
<br />

**[문제]** 싱글톤 안에서 프로토타입 Bean을 사용하는 경우에는, 원래 의도한 프로토타입으로 동작하지 않는다.

<details>
   <summary> 예시 소스코드 ⭐️(누르기) </summary>
<br />

*Single.java*
```java
@Component
public class Single {
    @Autowired
    Proto proto;

    public Proto callProto(){
        return proto;
    }
}
```

*ScopeRunner.java*
```java
@Component
public class ScopeRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Proto by Single");
        System.out.println(ctx.getBean(Single.class).callProto()); // Proto@61a002b1
        System.out.println(ctx.getBean(Single.class).callProto()); // Proto@61a002b1
        System.out.println(ctx.getBean(Single.class).callProto()); // Proto@61a002b1
    }
}
```

</details>

<br /><br />

**[해결]** 프로토타입 Bean을 직접 참조하는 것이 아니라, Proxy를 통해서 참조하도록 하면 된다.

<details>
   <summary> 예시 소스코드 ⭐️(누르기) </summary>
<br />

*Proto.java*

```java
@Component @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Proto {
}
```

*ScopeRunner.java*
```java
@Component
public class ScopeRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Proto by Single");
        System.out.println(ctx.getBean(Single.class).callProto()); // Proto@5bd1ceca
        System.out.println(ctx.getBean(Single.class).callProto()); // Proto@30c31dd7
        System.out.println(ctx.getBean(Single.class).callProto()); // Proto@499b2a5c
    }
}

```
</details>

<br />

cf) 참고 : https://en.wikipedia.org/wiki/Proxy_pattern



<br /><br />

--------------------------------------------------------------------------

# 1-5. Environment [👆](#목차)

: Test/Production과 같은 환경을 다루는 인터페이스로, @Profile, @Property 가 있다.

<br />

--------------------------------------------------------------------------
## @Profile 

: Bean들을 `test`일 때만 혹은 `prod`일 때만 사용될 수 있도록 설정할 수 있다.

<br />

### **[Profile 사용방법]**

### **1. Bean에 적용**

   : **클래스에 @Profile을 적용** 하거나 **메소드에 @Profile을 적용** 한다. (두가지 방법이 있음.)

   <br />

   1) **클래스** 에 적용

      ```java
      @Configuration @Profile("test")
      ```

      <details>
         <summary> 예시 코드 ⭐️(누르기) </summary>
      <br />

      *EnConfiguration.java*
      ```java
      @Configuration
      @Profile("test")  // 여기에 test, prod, !test, !prod, ...
      public class EnConfiguration {
         @Bean
         public BookService bookService(){
            return new BookService();
         }
      }
      ```
      </details>

   <br />

   2. **메소드** 에 적용

      ```java
      @Bean @Profile("test")
      ```

      <details>
         <summary> 예시 코드 ⭐️(누르기) </summary>
      <br />

      *EnConfiguration.java*

      ```java
      @Configuration
      public class EnConfiguration {
         @Bean 
         @Profile("test")  여기에 test, prod, !test, !prod, ...
         public BookService bookService(){
            return new BookService();
         }
      }
      ```

      <br />

      물론, 그냥 바로 줘도 됨.

      *BookService.java*

      ```java
      @Service
      @Profile("test")
      public class BookService {
         @Autowired
         BookRepository bookRepository;
      }
      ```

      </details>

<br />

### 2. **VM 설정하기**

   : Bean에 Profile을 등록했다면 다음으로 VM을 설정한다. (두가지 방법이 있음.)

   <details>
      <summary> 1번 방법 ⭐️(누르기) </summary>
   <br />

   <div align=center>
      <img src="./assets/2.png" />
   </div>

   </details>

   <br />
   

   <details>
      <summary> 2번 방법 ⭐️(누르기) </summary>
   <br />

   <div align=center>
      <img src="./assets/3.png" />
   </div>

   </details>






<br /><br />

--------------------------------------------------------------------------
## @Property

 - 어플리케이션에 등록할 수 있는 {key:value}에 접근할 수 있는 속성이다.
 - 프로퍼티에는 우선순위가 있다.
 - 값을 주는 방법은, VM option에 '-D' 로 줄 수 있다. (ex) -Dapp.name=spring5)

<br />

### **1. 값 확인하기**

: Environment 클래스로 확인할 수 있다.

*EnRunner.java*
```java
@Component
public class EnRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Environment environment = ctx.getEnvironment();
        System.out.println(environment.getProperty("spring.profiles.active"));
    }
}
```

<br />

### **2. 값 할당하기**

: **JVM 시스템 프로퍼티** 로 줄 수도 있고, **@PropertySource** 나 **@Value** 로 줄 수도 있다.

   1. JVM 시스템 프로퍼티 (-D로 주는 것)

      <details>
         <summary> 상세 ⭐️(누르기) </summary>
      <br />

      <div align=center>
         <img src="./assets/3.png" />
      </div>

      </details>
      <br />

   2. @PropertySource 

      <details>
         <summary> 상세 ⭐️(누르기) </summary>
      <br />

      *resource/app.properties*
      ```java
      spring.profiles.active=test
      ```

      *EnRunner.java*
      ```java
      @Component
      @PropertySource("classpath:/app.properties")  // 여기!
      public class EnRunner implements ApplicationRunner {

         @Autowired
         ApplicationContext ctx;

         @Override
         public void run(ApplicationArguments args) throws Exception {
            Environment environment = ctx.getEnvironment();
            System.out.println(environment.getProperty("spring.profiles.active"));
         }
      }
      ```
      
      </details>
      
      <br />

   3. @Value

      <details>
         <summary> 상세 ⭐️(누르기) </summary>
      <br />

      *EnRunner.java*
      ```java
      @Component
      public class EnRunner implements ApplicationRunner {

         @Value("${spring.profiles.active}") // 여기!
         String pro;

         @Override
         public void run(ApplicationArguments args) throws Exception {
            System.out.println(pro);
         }
      }
      ```

      </details>


   cf) 참고로, **우선순위는 JVM 시스템 프로퍼티가 높다.**



<br /><br />

--------------------------------------------------------------------------

# 1-6. MessageSource [👆](#목차)

: 메세지를 다국화하는 기능을 제공하는 인터페이스이다. 

 - ApplicationContext가 MessageSource를 가지고 있다.
 - Springboot를 사용하면 MessageSource는 자동으로 Bean으로 등록된다.

<br />

 ## 1. 사용법

 : 단어 맵핑을 리소스 번들로 설정해두면, MessageSource가 이를 구분하여 읽는다.

*resources/messages.properties*

```java
greeting=Hello, {0}
```

*resources/messages_ko_KR.properties*

```java
greeting=안녕, {0}
```

*AppRunner.java*

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    MessageSource messageSource;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Locale.setDefault(new Locale("en", "US"));

        // 넘겨줄 때, 파라미터도 함께 넘겨줄 수 있다.
        System.out.println(messageSource.getMessage("greeting", new String[]{"Sangjin"}, Locale.KOREA));   // 안녕, Sangjin
        System.out.println(messageSource.getMessage("greeting", new String[]{"Sangjin"}, Locale.getDefault()));  // Hello, Sangjin
    }
}
```



<br /><br />

--------------------------------------------------------------------------

# 1-7. ApplicationEventPublisher [👆](#목차)

: **이벤트 처리를 위한 인터페이스** 이다.

: 구조 : Publisher(이벤트 발생‼️) -> EventHandler(Event 처리)

- 역시 ApplicationConext는 ApplicationEventPublisher를 상속받는다.
- 이벤트를 발생시키는 주체는 ApplicationEventPublisher이다 .
- Event 클래스는 Bean으로 등록이 필요없다.
- Event 클래스는 스프링4.2까지는 ApplicationEvent를 상속받아야 했다. (지금은 X)
- EventHandler 클래스는 Bean으로 등록해야 한다.
- EventHandler 클래스는 스프링4까지는 ApplicationListener를 구현해야 했다. 

   (지금은 메서드에 @EventListener 라는 어노테이션을 단다.)

<br />

<details>
   <summary> 기본 예제 보기 ⭐️(누르기) </summary>
<br />

*MyEvent.java*
```java
public class MyEvent {
    private Object source;
    private int data;

    public MyEvent(Object source, int data){
        this.source = source;
        this.data = data;
    }

    public Object getSource() {
        return source;
    }

    public int getData() {
        return data;
    }
}
```

*MyEventHandler.java* 
```java
@Component
public class MyEventHandler {
    @EventListener
    public void myHandle(MyEvent event){
        System.out.println("이벤트 발생 / 데이터 : " + event.getData());
    }
}

```

*AppRunner.java*
```java
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationEventPublisher publisher;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        publisher.publishEvent(new MyEvent(this, 100));  // 이벤트 발생 / 데이터 : 100
    }
}

```

</details>

<br />

## **Handler가 2개라면**

: 하나의 이벤트를 2개 이상의 핸들러가 처리하는 구조일 때, 우선순위 주는 방법은 다음과 같다.

- @Order를 이용한다.
- 숫자가 낮은게 우선순위가 높다.

*MyHandler.java*
```java
@Component
public class MyEventHandler {
    @EventListener
    @Order(1)
    public void myHandle(MyEvent event){
        System.out.println("이벤트 발생 / 데이터 : " + event.getData());
    }
}
```

*AnotherHandler.java*
```java
@Component
public class AnotherHandler {
    @EventListener
    @Order(2)
    public void handle(MyEvent event){
        System.out.println("Another "+ event.getData());
    }
}
```



<br />

## **추가 Tips**

1. 한 Thread가 순차적으로 실행하는지, 여러 Thread가 개입하는지 확인하는 방법

   : 궁금한 코드에 현재 쓰레드를 출력해본다.

   ```java
   System.out.println(Thread.currentThread());
   ```

<br />

2. 비동기적으로 실행하는 방법

   : @Async를 사용한다. (ApplicationRunner에 @EnableAsync 추가해야함.)

   ```java
   @Component
   public class MyEventHandler {
      @EventListener
      @Async
      public void myHandle(MyEvent event){
         System.out.println("이벤트 발생 / 데이터 : " + event.getData());
      }
   }
   ```

<br />

3. 특별한 이벤트 핸들러

   (1) ContextRefreshedEvent

      : IoC컨테이너(ApplicationContext)가 시작될때 혹은 새로고침될때 발생한다.

      *MyHandler.java*
      ```java
      @EventListener
      public void handle(ContextRefreshedEvent event){
         System.out.println("ContextRefreshedEvent");
      }
      ```

   (2) ContextClosedEvent

      : 어플리케이션이 종료될때 발생한다.

      *MyHandler.java*
      ```java
      @EventListener
      public void handle(ContextClosedEvent event){
         System.out.println("ContextRefreshedEvent");
      }

      ```


<br /><br />

--------------------------------------------------------------------------

# 1-8. ResourceLoader [👆](#목차)

: 리소스를 읽어올 수 있는 인터페이스

ex)내가 만든 텍스트 파일 읽어올 수 있는 것.

<br />

<details>
   <summary> 기본 예제 보기 ⭐️(누르기) </summary>
<br />

*resources/test.txt*
```txt
hello spring
```

*AppRunner.java*
```java
@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    ResourceLoader loader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = loader.getResource("classpath:test.txt");
        if(resource.exists()){
            String content = Files.readString(Path.of(resource.getURI()));
            System.out.println(content);
        }
    }
}
```

<br />

cf) `classpath` 루트는 => `target/classes` 이다. 

</details>


<br /><br />

--------------------------------------------------------------------------

# 2. Resource 추상화 [👆](#목차)

 : 자바에서 리소스를 사용할 때, `java.net.URL` 로 한다. 그런데, 스프링 입장에서는 classpath로 리소스를 찾아가는 것도 필요하다. 그래서 `java.net.URL` 를 추상화한 `Resource` 라는 인터페이스가 등장했다. 


<br />

## **언제 쓰이는가?**

 : 우리가 알게 모르게 많은 곳에서 쓰인다. ApplicationContext를 만들 때, xml를 찾아서 만들게 되는데, xml을 찾는 방법이 여러개가 있다.

 (1) classpath를 기준으로 xml찾기

   *AppRunner.java*
   ```java
   var ctx = new ClassPathXmlApplicationContext("xxx.xml");  
   // "xxx.xml" 타입은 ClassPathResource
   ```

<br />

 (2) 파일시스템을 기준으로 xml찾기

   *AppRunner.java*
   ```java
   var ctx = new FileSystemXmlApplicationContext("xxx.xml");
   // "xxx.xml" 타입은 FileSystemResource
   ```
   

<br />

=> *위처럼 **Resource타입** 은 **ApplicationContext의 타입에 따라 결정** 된다.*

cf) 타입은 어떻게 확인할까?

<details>
   <summary> 예시 보기 ⭐️(누르기) </summary>
<br />

*AppRunner.java*
```java
@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(ctx.getClass()); 
               // AnnotationConfigServletWebServerApplicationContext
    }
}
```

</details>

<br /><br />

## **Resource 구현체**

- UrlResource
- ClassPathResource
- FileSystemResource
- ServletContextResource


<br />

## **사용 Tips**

: 리소스를 사용할 때는, **리소스 타입을 명시** 해주자!

- classpath:[위치]

- file:///[위치]

=> 만약, ClassPathXmlApplicationContext 라면, 리소스를 사용할 때, [위치]를 설명할 때 디폴트가 classpath기준이다.
하지만, 사용할 때는 꼭 리소스 타입을 명시해주자. 

*AppRunner.java* (ctx 타입과 상관없이)
```java
Resource resource = ctx.getResource("classpath:test.txt"); // classpath 기준일 때
Resource resource = ctx.getResource("file:///test.txt");   // 파일시스템 기준일 때
```

=> 대부분의 경우, WebApplicationContext라서 ServletContextResource를 사용하겠지만, 습관을 위처럼 들여두자.


<br /><br />

--------------------------------------------------------------------------

# 3. Validation 추상화 [👆](#목차)

 - 객체의 데이터를 검증하는 것이다. 
 - Validator 인터페이스를 구현하여 사용한다.
 - 두가지 메소드(`supports`, `validate`)를 구현해야 한다.
 - `Bean Validation` 이 제공하는 여러 `Validation용 어노테이션` 을 사용한다.

 cf) 참고 : https://docs.jboss.org/hibernate/beanvalidation/spec/2.0/api/

<br />

## 사용법

(1) supports

: 검증하려는 클래스가 Validator가 지원하는 클래스인지 확인하는 메소드이다.


<br />

(2) validate

: 실질적으로 검증 작업을 하는 메소드이다.


<br />

<details>
   <summary> (과거) 예시 보기 ⭐️(누르기) </summary>
<br />

*Event.java*
```java
public class Event {
   Integer id;
   String title;   // 이걸 검증해볼 예정 (빈 값 허용 X)
   Integer limit;
   String email;

   // getter, setter
}
```

*EventValidator.java*
```java
public class EventValidator implements Validator {
   @Override
   public boolean supports(Class<?> aClass) {
      return Event.class.equals(aClass);       // 검증하려는 aClass가 지원되는지 확인
   }

   @Override
   public void validate(Object o, Errors errors) {
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "notempty", "Empty filed is not allowed");  // title필드를 검증하고, notempty검증을 하는 것. (검증 종류는 위 링크 참고)
   }
}
```

*EventRunner.java*
```java
@Component
public class EventRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ======== Errors 구현 (Spring MVC에서는 알아서 해줌) ======= //
        Event event = new Event();
        Errors errors = new BeanPropertyBindingResult(event, "event"); // args: (Target Event, Name)


        EventValidator validator = new EventValidator();
        validator.validate(event, errors); // event의 검증 결과를 errors에 담음
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(e->{
                System.out.println("====== Error Code ======");
                Arrays.stream(e.getCodes()).forEach(System.out::println);
            });
        }
    }
}
```

</details>

<br />

<br />

<details>
   <summary> (요즘) 예시 보기 ⭐️(누르기) </summary>
<br />

*pom.xml*
```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

=> 의존성을 추가해줘야 validator가 `LocalValidatorFactoryBean` 타입이 된다.


<br />

*Event.java*
```java
public class Event {
    Integer id;

    @NotEmpty
    String title;

    @Min(0) @Max(100)
    Integer limit;

    @Email
    String email;

   // getter, setter
}
```

=> `@NotEmpty` 와 같은 어노테이션이 안 먹는다면, `System.out.println(validator.getClass());` 로 확인해보기~!

<br />


*EventRunner.java*
```java
@Component
public class EventRunner implements ApplicationRunner {
    @Autowired
    Validator validator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ======== Errors 구현 (Spring MVC에서는 알아서 해줌) ======= //
        Event event = new Event();
        event.setLimit(150);       // 일부러 error상황 유도
        event.setEmail("test");    // 일부러 error상황 유도
        Errors errors = new BeanPropertyBindingResult(event, "event"); // args: (Target Event, Name)

        validator.validate(event, errors); // event의 검증 결과를 errors에 담음
        if(errors.hasErrors()){
            errors.getAllErrors().forEach(e->{
                System.out.println("====== Error Code ======");
                Arrays.stream(e.getCodes()).forEach(System.out::println);
            });
        }
    }
}
```

</details>

<br />


<br /><br />

--------------------------------------------------------------------------

# 4. 데이터 바인딩 [👆](#목차)

```
사용자가 입력한 값을 어플리케이션 도메인 모델에 동적으로 변환해 넣어주는 기능이다.
```
*- 백기선 님의 설명 중*

=> 사용자는 대부분 문자열로 입력을 주지만, 우리 코드에서는 그게 Integer일 수도 있고, 다른 타입일 수도 있다. 따라서 **바인딩** 이 필요하다.

<br />

## 1. PropertyEditor

: 스프링 3.0 이전까지 사용했던 바인더 역할을 했던 인터페이스이다.

- Object와 String간의 변환만 할 수 있다.

- PropertyEditor가 가지고 있는 값은 Thread들끼리 공유가 된다. (Not Thread Safe)

- PropertyEditor 구현체 자체를 Bean으로 등록하면 안된다.

- 사용할 때는, Controller에서 바인더를 등록해서 사용하는 방법으로 해야한다.

<br />

<details>
   <summary> 사용 예시 보기 ⭐️(누르기) </summary>
<br />

*pom.xml*
```xml
<dependency>
   <groupId>junit</groupId>
   <artifactId>junit</artifactId>
   <version>4.12</version>
   <scope>test</scope>
</dependency>
```

=> Junit 의존성 추가해주기

<br />


*Event.java*
```java
public class Event {
   Integer id;
   String title;

   public Event(Integer id){
      this.id = id;
   }
   
   // getter, setter
}
```

*EventProperty.java*  (이걸 사용하는 예제)
```java
public class EventProperty extends PropertyEditorSupport {
   @Override
   public String getAsText() {  // Object -> String
      Event event = (Event)getValue();
      return event.getId().toString();
   }

   @Override
   public void setAsText(String text) throws IllegalArgumentException { // String -> Object
      setValue(new Event(Integer.parseInt(text)));
   }
}
```

=> getValue(), setValue() 에서 사용되는 Value가 PropertyEditor가 가지고 있는 값이다. 그런데, 이 값이 서로 다른 Thread에게 공유가 된다. 즉, Thread safe하지 않다. 따라서, PropertyEditor의 구현체들은 그냥 Bean으로 등록해서 사용해서는 안된다는 말이다. 

<br />

*EventController.java*
```java
@RestController
public class EventController {
   @InitBinder     // 그래서 Controller에서 바인더를 등록해서 사용.
   public void init(WebDataBinder webDataBinder){
      webDataBinder.registerCustomEditor(Event.class, new EventProperty());
   }

   @GetMapping("/event/{event}")
   public String getEvent(@PathVariable Event event){ // 사용자가 문자열로 입력하더라도 Event객체로 변환됨.
      return event.getId().toString();
   }
}
```

<br />

테스트 : Controller에서 cmd+N 하면, Test를 바로 만들 수 있음.

*EventControllerTest.java*
```java
@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTest {
   @Autowired
   MockMvc mockMvc;

   @Test
   public void getTest() throws Exception {
      mockMvc.perform(get("/event/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("1"));
   }
}
```

cf) import 필요
```java
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
```


</details>

<br />


<br />

## 2. Converter, Formatter

: PropertyEditor의 단점 극복을 위해 스프링 3.0부터 등장한 바인더 인터페이스이다.

### **Converter**

- `Converter<S, T>` : S타입을 T타입으로 변환해준다.
- 상태 정보를 가지지 않는다. (Thread Safe.)
- ConverterRegistry에 등록해서 사용한다.

<details>
   <summary> 사용 예시 보기 ⭐️(누르기) </summary>
<br />

*EventConverter.java*
```java
public class EventConverter {
   //String -> Event 변환
   public static class StringToEventConverter implements Converter<String, Event>{
      @Override
      public Event convert(String s) {
         return new Event(Integer.parseInt(s));
      }
   }

   // Event -> String 변환
   public static class EventToStringConverter implements Converter<Event, String>{
      @Override
      public String convert(Event event) {
         return event.getId().toString();
      }
   }
}
```

*WebConfig.java*
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
   @Override
   public void addFormatters(FormatterRegistry registry) {  // Converter 등록
      registry.addConverter(new EventConverter.StringToEventConverter());
   }
}
```

cf) 참고 : FormatterRegistry는 ConverterRegistry를 상속받고 있음.

</details>

<br />


### **Formatter**

- PropertyEditor를 대체하여 사용된다. (Thread Safe => Bean등록 가능)
- Object와 String간의 변환만 할 수 있다.
- 문자를 Locale에 따라 다국화할 수 있다.
- FormatterRegistry에 등록해서 사용한다.

<details>
   <summary> 사용 예시 보기 ⭐️(누르기) </summary>
<br />

*EventFormatter.java*
```java
public class EventFormatter implements Formatter<Event> {
   @Override
   public Event parse(String s, Locale locale) throws ParseException {
      return new Event(Integer.parseInt(s)); // String -> Object
   }

   @Override
   public String print(Event event, Locale locale) {
      return event.getId().toString();  // Object -> String
   }
}
```

*WebConfig.java*
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
   @Override
   public void addFormatters(FormatterRegistry registry) {
      registry.addFormatter(new EventFormatter());
   }
}
```

</details>

<br />

------------------------------------------------------------------
<details>
   <summary> 추가적인 이야기 ⭐️(누르기) </summary>
<br />

1. Converter와 Formatter가 모두 Thread Safe하기 때문에, 그냥 `@Component` 어노테이션 달아서 바로 Bean으로 등록해주자. 

      (WebConfig 따로 만들어서 등록해줄 필요 없음.)

      ex) 예시
      ```java
      @Component  // 여기!
      public static class StringToEventConverter implements Converter<String, Event>{
         @Override
         public Event convert(String s) {
            return new Event(Integer.parseInt(s));
         }
      }
      ```

<br />

2.  그리고 사실 생각해보면, `(@PathVariable Integer id)` 와 같은 식으로 사용할 때, 우리가 바인딩 처리를 해준 적이 없다. 이것은 스프링이 기본적으로 Integer와 몇가지 타입에 대해서 바인딩 처리를 알아서 해주는 것이다. 

      (다만, 내부적으로 바인딩 처리가 일어남을 알고 있는 것이 중요하다.)

<br />

3. 앞으로 사용하게 되더라도 스프링 부트가 제공하는 `WebConverterService` 를 사용하게 된다.

<div align=center>
   <img src="./assets/4.png" />
</div>



</details>

-----------------------------------------------------------------

<br />



<br /><br />

--------------------------------------------------------------------------

# 5. SpEL [👆](#목차)

: Spring Expression Language의 약자로, 

```
객체 그래프를 조회하고 조작하는 기능을 제공하는 표현언어이다.
```

=> 말이 조금 어렵지만, `@Value` 를 이용해서 변수를 사용하는 것이다~!!

그리고 문법은, thymeleaf를 사용해봤기 때문에 쉽게 받아들일 수 있었다. <a href="../Thymeleaf 정리.md">(thymeleaf 정리)</a>

<br />

## 사용법

**1. 표현식, Bean참조**
 
   ```
   #{"표현식"}
   ```
   <details>
      <summary> 예시 보기 </summary>
   <br />

   ```java
   @Value("#{1+1}")
   int value;

   @Value("#{'hello'+'world'}")
   String greeting;

   @Value("#{sample.data}")
   int sampleData;    // Bean으로 등록된 클래스의 데이터 참조
   ```

   *Sample.java*
   ```java
   @Component
   public class Sample {
      private int data = 200;

      public int getData() {
         return data;
      }

      public void setData(int data) {
         this.data = data;
      }
   }
   ```

   </details> 



<br />

**2. 변수 표현하기**
 
   ```
   ${"프로퍼티"}
   ```

   => application.properties에 변수를 담아두고 사용할 수 있다. (민감 정보도 이렇게~!)

   <details>
      <summary> 예시 보기 </summary>
   <br />

   *application.properties*
   ```properties
   my.value=100
   ```

   *AppRunner.java*
   ```java
   @Value("${my.value}")
   int myValue;
   ```
   

   </details>

<br />

cf) StandardEvaluationContext : Bean이 제공하는 함수도 쓸 수 있음.

<br /><br />

--------------------------------------------------------------------------

# 6. 스프링 AOP [👆](#목차)

: Aspect Oriented Programming의 약자로,

```
흩어진 Aspect를 모듈화할 수 있는 기법이다.
```
*-백기선님 설명 중*

=> 역시 나에게 와닿게 코멘트를 넣는다면, 모든 메서드의 속도를 측정하고자 할 때 사용하는 기법이다. 

## 1. 주요 용어

- Aspect : 특정 기능을 하는 모듈
- Target : Aspect를 적용할 클래스
- Advice : 특정 기능
- Join point : 메소드 실행 시점
- Pointcut : 어떤 Target에 적용

<br />

<div align=center>
   <img src="./assets/5.png" />
</div>

<br />

## 2. 사용법

### **(1) AspectJ**
- 컴파일
- 로드 타임

cf) 추가 설명은 <a href="../01-예제로 배우는 스프링 입문/필기.md">01-예제로 배우는 스프링 입문</a> 에 자세히 적어두었다.


--------------------------------------------------------------------------
<details>
   <summary> 해당 내용 보기 </summary>
<br />

### [ 사용 ]

### 1. 컴파일 단계 조작 (AspectJ) 

**A.java ------(AOP)-------> A.class**

: 컴파일 단계에서 AspectJ가 AOP를 구현해 주는 경우이다.

<br />

<div align=center>
  <img src="../01-예제로 배우는 스프링 입문/assets/2.png"
</div>

</div>
<br />


### 2. 바이트 코드 조작 (AspectJ) 

**A.java ----> A.class ----(AOP)----> 메모리**

: 클래스 로더가 클래스를 메모리에 올릴때, AspectJ가 AOP를 구현해 주는 경우이다.

<br />

<div align=center>
  <img src="../01-예제로 배우는 스프링 입문/assets/3.png"
</div>

</div>

<br />

</details>

--------------------------------------------------------------------------


<br /><br />

cf) 실제로는, 스프링 AOP형태로 많이 사용되기 때문에, 스프링 AOP에 대해서 설명하겠다.

### **(2) 스프링 AOP**
- 런타임에 적용된다.
- 프록시 기반의 AOP 구현체이다.
- 스프링 Bean에만 AOP를 적용할 수 있다.
- 모든 AOP문제가 아닌, 대표적인(흔한) 문제를 해결하기 위한 용도로 쓰인다.

<br />

⭐️ 자꾸 등장하는 **프록시** 는 무엇일까?

=> A class에 접근하려고 할 때, 바로 가지 못하고 A class를 감싸고 있는 껍데기(이게 프록시)를 통해야 한다.


<br />

   ### **1. 프록시**

   ```
   기존 코드 변경 없이 `접근제어` 혹은 `부가기능` 을 추가하는 것이다.
   ```

   <details>
      <summary> 기존 코드 보기 </summary>
   <br />

   => 아래 **코드를 하나도 건드리지 않고**, **(시간측정과 같은) 기능을 추가** 할 수 있다.

   *EventService.java*
   ```java
   public interface EventService {
      public void createEvent();
      public void publishEvent();
      public void deleteEvent();
   }
   ```

   *SimpleEventService.java*
   ```java
   @Service
   public class SimpleEventService implements EventService{

      @Override
      public void createEvent() {  // 걸리는 시간이 궁금한 상황 1
         System.out.println("Create the Event");
      }

      @Override
      public void publishEvent() { // 걸리는 시간이 궁금한 상황 2
         System.out.println("Publish the Event");
      }

      @Override
      public void deleteEvent() {  // 이건 관심 없는 상황
         System.out.println("Delete the Event");
      }
   }  
   ```

   *EventRunner.java*
   ```java
   @Component
   public class EventRunner implements ApplicationRunner {
      @Autowired
      EventService eventService;  // Autowired는 되도록 Interface로 받기!!

      @Override
      public void run(ApplicationArguments args) throws Exception {
         eventService.createEvent();
         eventService.publishEvent();
         eventService.deleteEvent();
      }
   }
   ```

   </details>

   <br />

   <details>
      <summary> 프록시 기반 AOP 코드 보기 </summary>
   <br />

   *ProxySimpleEventService.java*
   ```java
   @Primary    // EventRunner는 SimpleEventService가 아니라 프록시를 주입받게 되는 것이다.
   @Service
   public class ProxySimpleEventService implements EventService{

      @Autowired
      SimpleEventService simpleEventService;  // 원래 실행할거는 그대로 해야하니까

      @Override
      public void createEvent() {
         Long begin = System.currentTimeMillis();
         simpleEventService.createEvent();
         System.out.println(System.currentTimeMillis() - begin);
      }

      @Override
      public void publishEvent() {
         Long begin = System.currentTimeMillis();
         simpleEventService.publishEvent();
         System.out.println(System.currentTimeMillis() - begin);
      }

      @Override
      public void deleteEvent() {
         simpleEventService.deleteEvent();
      }
   }
   ```

   => 이 때, 프록시 타입은 접근하려고 하는 `SimpleEventService` 의 타입과 완전히 같아야 함. (상속도 같아야함.)

   </details>
   
   => 하지만, 이것도 클래스를 하나하나 만들어야 하는 단점이 있다.

   <br />

   <details>
      <summary> 스프링 AOP 코드 보기 - [방법1] </summary>
   <br />

   => `AbstractAutoProxyCreator` 가 자동으로 프록시로 클래스를 감싸주는게 내부 동작이다.

   스프링 어노테이션 기반의 AOP를 위해서는 의존성을 추가해줘야한다.

   *pom.xml*
   ```xml
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-aop</artifactId>
   </dependency>
   ```
   
   <br />
   
   *PerfAspect.java* (Advice와 Pointcut을 정의해야한다.)
   ```java
   @Component
   @Aspect
   public class PerfAspect {

      @Around("execution(* com.example.webtest.EventService.*(..))")    // Pointcut을 정의하는 곳(모든 메소드)
      public Object logPerf(ProceedingJoinPoint pip) throws Throwable { // pip : 'target 메소드' 를 감싸고 있다.
         Long begin = System.currentTimeMillis();  // Advice인데, @Around라서 target메소드를 감싸는 형식이다.
         Object ret = pip.proceed();   // target의 메소드를 실행한 결과
         System.out.println(System.currentTimeMillis() - begin);
         return ret;
      }
   }
   ```

   <br />

   </details>


   <br />

   <details>
      <summary> 스프링 AOP 코드 보기 - [방법2] </summary>
   <br />

   => Aspect를 넣을 메소드에 어노테이션을 다는 방법이다.
   
   *PerfAspect.java* (Advice와 Pointcut을 정의해야한다.)
   ```java
   @Component
   @Aspect
   public class PerfAspect {

      @Around("@annotation(PerfLogging)")    // PerfLogging 이라는 어노테이션이 있는 곳에 적용
      public Object logPerf(ProceedingJoinPoint pip) throws Throwable {
         Long begin = System.currentTimeMillis();
         Object ret = pip.proceed();
         System.out.println(System.currentTimeMillis() - begin);
         return ret;
      }
   }
   ```

   <br />

   *PerfLogging.java*
   ```java
   @Documented // javadoc을 위해서
   @Target(ElementType.METHOD) // 메소드 대상으로 적용한다.
   @Retention(RetentionPolicy.CLASS) // 사라지는 타이밍. SOURCE하면 컴파일 후에 Aspect 사라짐. (최소 CLASS, RUNTIME)
   public @interface PerfLogging {
   }
   ```

   <br />

   => 그리고 Aspect 넣을 메소드에 어노테이션을 붙여주면 끝이다.

   *SimpleEventService.java*
   ```java
   @Service
   public class SimpleEventService implements EventService{

      @Override
      @PerfLogging   // 여기
      public void createEvent() {
         System.out.println("Create the Event");
      }

      @Override
      @PerfLogging   // 여기
      public void publishEvent() {
         System.out.println("Publish the Event");
      }

      @Override
      public void deleteEvent() {
         System.out.println("Delete the Event");
      }
   }
   ```

   </details>


   <br />

   <details>
      <summary> 스프링 AOP 코드 보기 - [방법3] </summary>
   <br />

   => bean으로 적용할 수도 있다.
   
   <br />
   
   *PerfAspect.java* 
   ```java
   @Component
   @Aspect
   public class PerfAspect {

      @Around("bean(simpleEventService)") // bean에 있는 모든 메소드에 적용
      public Object logPerf(ProceedingJoinPoint pip) throws Throwable {
         Long begin = System.currentTimeMillis();
         Object ret = pip.proceed();
         System.out.println(System.currentTimeMillis() - begin);
         return ret;
      }
   }
   ```

   <br />

   </details>

   <br />

   cf) Advice 종류

   - @Before
   - @AfterReturning
   - @AfterThrowing
   - @Around



<br /><br />

--------------------------------------------------------------------------

# 7. Null-Safety [👆](#목차)

```
컴파일 단계에서 nullpoint exception을 최대한 방지하기 위한 것이다.
```

=> null을 허용하는지 안하는지 어노테이션으로 표시하자.

<br />

## 사용법

### **[ 어노테이션 ]**

- @NonNull
- @Nullable
- @NonNullApi (패키지 단위로 적용)
- @NonNullFields (패키지 단위로 적용)

<br />

<details>
   <summary> 예시 보기 </summary>
<br />

*EventService.java*
```java
@Service
public class EventService {
    @NonNull  // 반환 값에 Null허용 X
    public String helloName(@NonNull String name){  // 파라미터 Null허용 X
        return "Hello, " + name;
    }
}
```


*EventRunner.java*
```java
@Component
public class EventRunner implements ApplicationRunner {
    @Autowired
    EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String greeting = eventService.helloName(null);  // 이러면 intellij가 틀렸다고 함.
    }
}
```

</details>

<br />

cf) 단, intellij 사용자의 경우, 에러표시가 따로 나지 않는 경우 다음 설정이 필요합니다.

<details>
   <summary> intellij 설정 보기 </summary>
<br />

<div align=center>
   <img src="./assets/6.png" />
</div>

</details>






<br /><br /><br />

# 메모

### 1. mock 객체

   : 테스트 코드를 작성할 때, **의존성 문제**로 특정 객체를 생성하는데 어려운 경우 만드는 **가짜 객체**이다.

   [사용법]

   - mock객체를 만든다. 
     - mock() 함수 이용
     - @Mock이용
   - when(A).thenReturn(B); 

   [예시]

   ```java
   @Mock
   BookRepository bookRepository; // cal메소드 : 10을 반환하는 메소드
   
   @Test
   public void calTest(){
     when(bookRepository.cal()).thenReturn(10);
   }
   ```

<br />

### 2. @WebTest 사용하기

   : 기본적으로 Controller들만 주로 Bean 등록이 된다. 
    따라서, Converter나 Formatter처럼 특수한 기능 테스트를 할 때는 파라미터로 지정해줘야 한다.

   *EventControllerTest.java*
   ```java
   @RunWith(SpringRunner.class)
   @WebTest({EventController.class, EventConverter.StringToEventConverter.class})
   public class EventControllerTest {
      @Autowired
      MockMvc mockMvc;

      @Test
      public void getTest() throws Exception {
         mockMvc.perform(get("/event/1"))
                  .andExpect(status().isOk())
                  .andExpect(content().string("1"));
      }
   }
   ```
   
<br />

### 3. 서버모드 없애기

   : 웹이 아닌, 그냥 java main함수 실행처럼 실행시킬 수 있다.

   *WebtestApplication.java*
   ```java
   @SpringBootApplication
   public class WebtestApplication {

      public static void main(String[] args) {
         SpringApplication app = new SpringApplication(WebtestApplication.class);
         app.setWebApplicationType(WebApplicationType.NONE); // 여기서 주의
         app.run(args);
      }
   }
   ```

   또는
   *application.properties*
   ```java
   spring.main.web-application-type=none
   ```
