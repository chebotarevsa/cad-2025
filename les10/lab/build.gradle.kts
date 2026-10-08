plugins {
    java
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.4")
    implementation("org.springframework:spring-orm:6.2.4")
    implementation("org.springframework:spring-web:6.2.4")
    implementation("org.springframework.data:spring-data-jpa:3.4.4")

    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    runtimeOnly("com.h2database:h2:2.2.224")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    providedCompile("jakarta.servlet:jakarta.servlet-api:6.1.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.war {
    archiveFileName.set("pet-store.war")
}
