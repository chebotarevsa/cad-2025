plugins {
    java
    war
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.7")
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation("org.springframework:spring-orm:6.1.5")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("com.h2database:h2:2.2.224")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("ch.qos.logback:logback-classic:1.5.13")
    implementation("org.springframework:spring-webmvc:6.2.2")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")
    implementation("org.springframework:spring-web:6.2.4")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.13.0")
    implementation("org.springframework.security:spring-security-web:6.4.4")
    implementation("org.springframework.security:spring-security-config:6.3.5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.1")
    testImplementation("org.mockito:mockito-core:5.17.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.17.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.5") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.springframework:spring-test:6.1.5")
    testImplementation("com.h2database:h2:2.2.224")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.war {
    archiveFileName.set("petstore.war")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.required = true
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco/html")
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/config/**",
                    "**/entity/**",
                    "**/dto/**",
                    "**/exception/**",
                    "**/*Application*",
                    "**/ServletInitializer*"
                )
            }
        })
    )
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }

        rule {
            element = "CLASS"
            includes = listOf("ru.bsuedu.cad.lab.service.*")
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}