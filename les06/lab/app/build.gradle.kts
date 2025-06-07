plugins {
    // Плагин Spring Boot
    id("org.springframework.boot") version "2.7.5"
    id("java")
    application
}

repositories {
    // Используем Maven Central для загрузки зависимостей
    mavenCentral()
}

dependencies {
    // Зависимости Spring Boot для работы с базой данных, логированием и AOP
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:2.7.5")
    implementation("org.springframework.boot:spring-boot-starter-logging:2.7.5")
    implementation("org.springframework.boot:spring-boot-starter:2.7.5")
    implementation("com.h2database:h2:2.1.214") // Указание точной версии H2
    implementation("org.springframework.boot:spring-boot-starter-aop:2.7.5")

    // Зависимости для тестирования
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.5")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Версия Java
    }
}

application {
    // Указываем главный класс для запуска приложения
    mainClass.set("ru.bsuedu.cad.lab.App")
}

tasks.named<Test>("test") {
    // Используем JUnit Platform для выполнения тестов
    useJUnitPlatform()
}

// Убедитесь, что ресурсы будут доступны при запуске
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    classpath = sourceSets.main.get().runtimeClasspath
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
