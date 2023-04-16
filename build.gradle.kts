plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    application
}

group = "dev.yuua"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://repo.yuua.dev/")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.6")
    implementation("com.github.minndevelopment:jda-ktx:9fc90f6")
    implementation("dev.yuua:journeylib:2.10.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("com.github.ajalt.clikt:clikt:3.5.1")
    implementation("com.github.walkyst:lavaplayer-fork:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.ktor:ktor-client-core:2.2.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")
    implementation("io.ktor:ktor-client-cio-jvm:2.2.4")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}

val `package` = task("package", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("Tone")
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks {
    `package`
}