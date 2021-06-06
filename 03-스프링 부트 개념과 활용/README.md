# 목차

1. [세팅](#1-세팅-)
<br /></t>
2. [ApplicationContext](#1-1-applicationcontext-)
<br /></t>


<br /><br /><br />

--------------------------------------------------------------------------

# 1. 세팅 [👆](#목차)

## 1. 스프링 부트 환경 설정

이 부분은 필요한 사람들만 보면 된다.

Intellij의 'Spring Initializr' 가 아닌, **자바 프로젝트를 만들어서 부트를 실행하는 방법** 이다. 



<details >
    <summary> 상세 방법 ⭐️(누르기)</summary>
<br />

1. **프로젝트 만들기**

    : Maven으로 해도 되고, Gradle로 해도 됩니다. (지금은 Maven 사용 예정)

<div align=center>
   <img src="./assets/1.png" />
</div>

<br />

2. **이름 정하기**

    - Groupid : 패키지명
    - Artifact : 프로젝트 빌드명

<div align=center>
   <img src="./assets/2.png" />
</div>

<br />

3. **pom.xml 설정하기**

    : maven간에는 프로젝트 계층 구조를 만들 수 있다.

    - `<parent>` : 부모 프로젝트를 `spring-boot-parent`로 설정해주는 것이다.
    - `<build>`  : 패키징을 어떻게 할 것인지 설정해주는 것이다.


    *pom.xml*
    ```xml
    <parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.3.RELEASE</version>
	</parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
    ```

    cf) <a href="https://docs.spring.io/spring-boot/docs/2.0.3.RELEASE/reference/htmlsingle/#getting-started-maven-installation" target="_blank"> 참고 </a>

<br />

4. **스프링 부트 실행**

    *Application.java*
    ```java
    package me.sangjin.bootinit;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }
    ```

    두가지 방법으로 실행할 수 있다.

    방법-1) 우클릭 후, `Run Application.java` 로 실행한다.

    방법-2) 자바 패키지로 실행한다.

        ```
        mvn package
        java -jar target/bootStarter-1.0-SNAPSHOT.jar
        ```

    

    <details>
        <summary> cf) `zsh: command not found: mvn` 뜨는 경우 </summary>
    <br />

        ```
        brew install maven
        mvn -version         # 버전확인
        brew info maven      # 위치확인
        ```

    <br />
    </details>

    <br />


    한 가지만 주의할 것은, `SpringbootApplication`은 `java`폴더 바로 밑에 두지 말고, 패키지를 만들어서 두자.
    
    (프로젝트 안에 있는 모든 자바 파일을 컴포넌트 스캔할 필요는 없을 것.)



</details>

<br />

(추천 방법 확인하기 => <a href="https://github.com/osj3474/Spring-Note/tree/main/03-%EC%8A%A4%ED%94%84%EB%A7%81%20%EB%B6%80%ED%8A%B8%20%EA%B0%9C%EB%85%90%EA%B3%BC%20%ED%99%9C%EC%9A%A9/settings">깃헙 이동</a>)





<br /><br /><br />

--------------------------------------------------------------------------

# 2. ? [👆](#목차)

## 1. 





