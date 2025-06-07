plugins {
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Основные зависимости
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("com.h2database:h2:2.2.224")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Для тестов (JUnit Jupiter)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.12.1")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    // Указываем главный класс
    mainClass = "ru.bsu.cad.lab.app.Main"
}
