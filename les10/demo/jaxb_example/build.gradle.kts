plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")
    implementation("javax.activation:activation:1.1.1")
}

application {
    mainClass.set("com.example.Main")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}