plugins {
    application
    java
}

group = "ru.bsuedu.cad.lab"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.8.2")

    // Spring
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("org.springframework:spring-context:6.1.6")
    implementation("org.springframework:spring-aop:6.1.6") // для @EnableAspectJAutoProxy

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-aop:2.6.7")

    // AspectJ
    implementation("org.aspectj:aspectjrt:1.9.7")
    implementation("org.aspectj:aspectjweaver:1.9.7")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "ru.bsuedu.cad.lab.App"
}

tasks.named<Test>("test") {
    enabled = false
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs("-Dfile.encoding=UTF-8")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
