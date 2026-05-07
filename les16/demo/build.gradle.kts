plugins {
    java
    war
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.0.0")
    implementation("org.springframework:spring-orm:6.0.0")
    implementation("org.springframework:spring-webmvc:6.0.0")
    implementation("org.springframework.data:spring-data-jpa:3.0.0")
    implementation("org.hibernate.orm:hibernate-core:6.1.7.Final")
    implementation("com.zaxxer:HikariCP:5.0.1")
    runtimeOnly("com.h2database:h2:2.1.214")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}