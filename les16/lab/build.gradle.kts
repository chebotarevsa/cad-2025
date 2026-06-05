plugins {
    java
    war
    application
    jacoco
}

group = "ru.bsuedu.cad"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.1.5")
    implementation("org.springframework:spring-web:6.1.5")
    implementation("org.springframework:spring-webmvc:6.1.5")
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    // implementation("org.springframework.orm:spring-orm:6.1.5")
    implementation("org.springframework.security:spring-security-web:6.1.5")
    implementation("org.springframework.security:spring-security-config:6.1.5")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")
    implementation("com.h2database:h2:2.2.224")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
    testImplementation("org.springframework:spring-test:6.1.5")
    testImplementation("org.mockito:mockito-core:5.6.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.6.0")
    testImplementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
}

application {
    mainClass.set("ru.bsuedu.cad.lab.App")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)
    }
}
