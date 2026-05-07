plugins {
    id("java")
    id("war")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.aliyun.com/repository/public")
    }
}

dependencies {
    // Spring Framework
    implementation("org.springframework:spring-context:6.0.0")
    implementation("org.springframework:spring-orm:6.0.0")
    implementation("org.springframework:spring-webmvc:6.0.0")

    // Spring Data JPA
    implementation("org.springframework.data:spring-data-jpa:3.0.0")

    // Hibernate ORM
    implementation("org.hibernate.orm:hibernate-core:6.1.7.Final")

    // HikariCP
    implementation("com.zaxxer:HikariCP:5.0.1")

    // H2 Database
    runtimeOnly("com.h2database:h2:2.1.214")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Jakarta
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    // Servlet API (provided by Tomcat)
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // Thymeleaf for Spring 6
    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<War> {
    archiveBaseName.set("cafe-app")
    archiveVersion.set("")
}