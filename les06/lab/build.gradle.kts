plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.1.4")
    implementation("org.springframework:spring-jdbc:6.1.4")
    implementation("org.springframework:spring-aop:6.1.4")
    implementation("org.aspectj:aspectjweaver:1.9.20.1")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("com.h2database:h2:2.2.224")
    implementation("ch.qos.logback:logback-classic:1.4.14")
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

tasks.withType<JavaExec> {
    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}
