plugins {
    java
    id("com.gradleup.shadow")
    id("io.github.intisy.github-gradle")
}

group = "io.github.sefiraat"
version = "1.0.0-UNOFFICIAL"
description = "Networks is a Slimefun addon that brings item storage and transportation networks."

github {
    accessToken = System.getenv("GITHUB_TOKEN") ?: ""
    publish {
        tag = System.getenv("GITHUB_REF_NAME")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://nexus.neetgames.com/repository/maven-public/")
    maven("https://repo.bg-software.com/repository/api/")
}

dependencies {
    compileOnly(files("../../core/Slimefun5/core/build/libs/Slimefun v5.0.0-UNOFFICIAL-MC26.1.2.jar"))
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.jetbrains:annotations:23.0.0")

    compileOnly(files("../InfinityExpansion/build/libs/InfinityExpansion v1.0.0.jar"))
    compileOnly("com.bgsoftware:WildChestsAPI:2024.1")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveFileName.set("Networks v${project.version}.jar")
        exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
    compileTestJava {
        enabled = false
    }
    test {
        enabled = false
    }
}
