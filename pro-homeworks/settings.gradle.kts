rootProject.name = "pro-homeworks"
include("hw01-gradle")
pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}
include("hw02-collections")
include("hw03-annotations")
include("hw04-heap")
include("hw05-classloader-autolog")
include("hw05-autolog-classloader")
include("hw06-bankomat")
include("hw07-message-processor")
include("hw08-json-handler")
include("hw09-custom-orm:demo")
include("hw09-custom-orm:homework")
include("hw10-jpql-hibernate:homework")
include("hw11-cache")
include("hw12-web")
include("hw13-ioc")
include("hw14-springboot")
include("hw15-executors")
include("hw16-queues")
