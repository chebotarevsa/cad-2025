plugins {
    war
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.mockito:mockito-core:5.17.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.17.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.springframework:spring-test:6.2.2")

    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation(libs.spring.context)
    implementation(libs.spring.webmvc)
    implementation(libs.spring.orm)
    implementation(libs.spring.data.jpa)
    implementation(libs.hibernate.core)
    implementation(libs.hikari)
    implementation(libs.h2)
    implementation(libs.logback.classic)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.jsr310)
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.2.RELEASE")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.war {
    archiveFileName.set("ROOT.war")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}
