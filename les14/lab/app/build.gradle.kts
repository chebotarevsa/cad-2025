plugins {
    java
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.7")
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation("org.springframework:spring-orm:6.1.5")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("com.h2database:h2:2.2.224")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("org.springframework:spring-webmvc:6.2.2")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")
    implementation("org.springframework:spring-web:6.2.4")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.13.0")
    implementation("org.springframework.security:spring-security-web:6.4.4")
    implementation("org.springframework.security:spring-security-config:6.3.5")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.war {
    archiveFileName.set("petstore.war")
}
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
