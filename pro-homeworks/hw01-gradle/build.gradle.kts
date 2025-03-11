import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
}

dependencies {
   implementation("com.google.guava:guava")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("HelloOtus")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = "hello.HelloOtus"
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
