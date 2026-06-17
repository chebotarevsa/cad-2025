plugins {
    java
    application
}

group = "lab1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.1.5")
    implementation("org.springframework:spring-aop:6.1.5")
    implementation("org.aspectj:aspectjweaver:1.9.21")
    implementation("org.aspectj:aspectjrt:1.9.21")

    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
}

application {
    mainClass.set("lab1.MainApp")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}