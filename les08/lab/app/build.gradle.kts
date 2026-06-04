plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.context)
    implementation(libs.spring.orm)
    implementation(libs.spring.data.jpa)
    implementation(libs.hibernate.core)
    implementation(libs.jakarta.persistence.api)
    implementation(libs.hikari)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    runtimeOnly(libs.h2)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "ru.bsuedu.cad.lab.app.App"
}
