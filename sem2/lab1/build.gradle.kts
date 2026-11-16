plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
    jacoco
}


group = "ru.raspisa"
version = "0.0.1-SNAPSHOT"


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // starter-web Spring MVC + встроенный Tomcat + JSON (Jackson)
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // starter-data-jpa JPA + Hibernate + Spring Data 
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    // BCrypt
    implementation("org.springframework.security:spring-security-crypto")
    
    // web push 
    implementation("nl.martijndwars:web-push:5.1.1")

    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")

    // bd
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    
    implementation(platform("me.paulschwarz:spring-dotenv-bom:5.1.0"))
    implementation("me.paulschwarz:springboot4-dotenv")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


jacoco {
    toolVersion = "0.8.12"
}

// tasks.named<Test>("test") {
//     useJUnitPlatform()
//     finalizedBy(tasks.named("jacocoTestReport"))
// }

// tasks.named<JacocoReport>("jacocoTestReport") {
//     dependsOn(tasks.named("test"))
//     reports {
//         xml.required.set(false)
//         csv.required.set(false)
//         html.required.set(true)
//         html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
//     }
// }

// jar 
val npmCommand = if (System.getProperty("os.name").lowercase().contains("win")) "npm.cmd" else "npm"
val frontendDir = layout.projectDirectory.dir("frontend")

npm install — val npmInstall by tasks.registering(Exec::class) {
    workingDir = frontendDir.asFile
    commandLine(npmCommand, "install")
    inputs.file(frontendDir.file("package-lock.json"))
    outputs.dir(frontendDir.dir("node_modules"))
}

// npm run build 
val npmBuild by tasks.registering(Exec::class) {
    dependsOn(npmInstall)
    workingDir = frontendDir.asFile
    commandLine(npmCommand, "run", "build")
    inputs.dir(frontendDir.dir("src"))
    inputs.file(frontendDir.file("package.json"))
    inputs.file(frontendDir.file("vite.config.js"))
    inputs.file(frontendDir.file("index.html"))
    outputs.dir(frontendDir.dir("dist"))
}

//static внутри jar
tasks.processResources {
    dependsOn(npmBuild)
    from(frontendDir.dir("dist")) {
        into("static")
    }
}
