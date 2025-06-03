plugins {
    war
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Сервлет API
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // Spring
    implementation("org.springframework:spring-context:6.1.5")
    implementation("org.springframework:spring-orm:6.1.5")
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation("org.springframework:spring-web:6.1.5")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("com.zaxxer:HikariCP:5.1.0")

    runtimeOnly("com.h2database:h2:2.2.224")
    implementation("com.h2database:h2:2.2.224")

    // OpenCSV
    implementation("com.opencsv:opencsv:5.9")

    // JSON (если нужен REST)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")

    // Логгирование
    implementation("org.slf4j:slf4j-api:2.0.12")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.14")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    //  зависимости Spring Web + Thymeleaf
    implementation("org.springframework:spring-webmvc:6.1.5")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")

     // Spring Security (Убедись, что версии указаны!)
    implementation("org.springframework.security:spring-security-web:6.1.5")
    implementation("org.springframework.security:spring-security-config:6.1.5")
    implementation("org.springframework.security:spring-security-core:6.1.5")
    implementation("org.springframework.security:spring-security-crypto:6.1.5")

    // Для создания пользователей в памяти
    implementation("org.springframework.security:spring-security-core:6.1.5")

    // Thymeleaf для работы с security (опционально)
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE")

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<Jar> {
    archiveBaseName.set("zoo-shop-app")
    archiveVersion.set("1.0.0")
    
}

tasks.withType<War> {
    archiveFileName.set("zoo-shop.war")
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

sourceSets {
    named("main") {
        resources {
            srcDir("src/main/resources")
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<ProcessResources> {
    from("src/main/webapp") {
        include("**/*.html")
        into("WEB-INF/templates")
    }
}