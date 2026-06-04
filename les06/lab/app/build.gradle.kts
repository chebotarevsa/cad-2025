plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.spring.context)
    implementation(libs.spring.jdbc)
    implementation(libs.aspectjweaver)
    implementation(libs.h2)
    implementation(libs.logback.classic)
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "ru.bsuedu.cad.lab.App"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    jvmArgs("-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8")
}
