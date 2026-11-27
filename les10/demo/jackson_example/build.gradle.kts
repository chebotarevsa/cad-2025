plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
}

application {
    mainClass.set("com.example.Main")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}