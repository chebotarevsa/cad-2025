plugins {
    java
    war
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2:2.2.224")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    implementation("com.zaxxer:HikariCP:5.1.0")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    implementation("com.opencsv:opencsv:5.9")

    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.bootWar {
    archiveFileName.set("zoo-shop.war")
}

tasks.war {
    archiveFileName.set("zoo-shop.war")
}