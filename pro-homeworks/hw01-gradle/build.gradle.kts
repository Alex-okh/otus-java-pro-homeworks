import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1" // Add the Shadow plugin
}

group = "ru.otus"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.guava:guava:33.4.0-jre")
}

tasks.test {
    useJUnitPlatform()
}

// Configure the ShadowJar task
tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("HelloOtus")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = "HelloOtus"
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
