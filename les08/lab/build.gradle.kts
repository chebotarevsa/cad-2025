plugins {
    java
    application
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "ru.bsuedu.cad"
version = "1.0-SNAPSHOT"
application {
    mainClass.set("ru.bsuedu.cad.lab.app.MainApp")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.9")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
