plugins {
    java
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.1.5")
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation("org.springframework:spring-orm:6.1.5")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("com.h2database:h2:2.2.224")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    implementation("org.springframework:spring-web:6.2.4")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.war {
    archiveFileName.set("petstore.war")
}