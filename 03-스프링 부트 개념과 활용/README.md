# 목차

1. [세팅](#1-세팅-)
<br /></t>
2. [ApplicationContext](#1-1-applicationcontext-)
<br /></t>


<br /><br /><br />

1. 세팅

Groupid : 패키지명
Artifact : 프로젝트 빌드명

https://www.baeldung.com/spring-boot-starter-parent

이걸 쭉 따라갈거임.

*pom.xml*
```xml
<parent>
    <groupId>com.baeldung</groupId>
    <artifactId>spring-boot-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</parent>
```

maven간에는 프로젝트 계층 구조를 만들 수 있음.
부모 프로젝트를 `spring-boot-parent`로 설정해주는 것.
