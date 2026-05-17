plugins {
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-jdbc:6.2.2")
    implementation("org.springframework.data:spring-data-jpa:3.4.4")
    implementation("org.springframework:spring-webmvc:6.2.2")
    implementation("org.springframework.security:spring-security-web:6.2.2")
    implementation("org.springframework.security:spring-security-config:6.2.2")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("org.hibernate.orm:hibernate-core:6.6.8.Final")
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.3.RELEASE")
    implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")
    implementation("ch.qos.logback:logback-classic:1.5.12")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.3.RELEASE")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}