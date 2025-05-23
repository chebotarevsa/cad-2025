plugins {
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.2.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    mainClass.set("ru.bsuedu.cad.lab.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
}
