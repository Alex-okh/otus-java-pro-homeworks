plugins {
    id("java")
}

group = "ru.otus"
version = "unspecified"

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation ("ch.qos.logback:logback-classic")
    implementation("com.google.guava:guava")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.glassfish:jakarta.json:2.0.1")
    implementation("com.google.protobuf:protobuf-java-util")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.google.code.gson:gson")

    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.assertj:assertj-core:3.25.3")
}

tasks.test {
    useJUnitPlatform()
}