plugins {
    java
    id("com.gradleup.shadow")
    id("io.github.intisy.github-gradle") version "1.8.3"
}

group = "io.github.sefiraat"
description = "Networks is a Slimefun addon that brings item storage and transportation networks."

// Shared Slimefun-addon build conventions (Java 8, spigot-api baseline, core dep, publish, shadow, version).
apply(from = "https://raw.githubusercontent.com/Slimefun5/workflows/stable/slimefun-addon.gradle")

repositories {
    maven("https://jitpack.io")
    maven("https://nexus.neetgames.com/repository/maven-public/")
    maven("https://repo.bg-software.com/repository/api/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
    githubCompileOnly("Slimefun5:InfinityExpansion:v1.1.2")
    compileOnly("com.bgsoftware:WildChestsAPI:2024.1")
}

tasks {
    compileTestJava { enabled = false }
    test { enabled = false }
}
