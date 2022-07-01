val kotlin_version: String by project
val ktorm_version: String = "3.5.0"
plugins {
    application
//    kotlin("jvm") version "1.7.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
}

group = "umb.ink.ktor"
version = "0.0.1"
application {
    mainClass.set("umb.ink.ktor.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("androidx.annotation:annotation:1.4.0")
    implementation("org.ktorm:ktorm-core:${ktorm_version}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // mirai
    implementation("net.mamoe:mirai-core:2.11.1")
    api("org.jetbrains.kotlinx","kotlinx-coroutines-core","1.3.9")
}