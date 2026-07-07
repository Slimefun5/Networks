plugins {
    java
    id("com.gradleup.shadow")
    id("io.github.intisy.github-gradle") version "1.8.3"
}

group = "io.github.sefiraat"
description = "Networks is a Slimefun addon that brings item storage and transportation networks."

apply(from = "https://raw.githubusercontent.com/Slimefun5/gradle/stable/slimefun-addon.gradle")

repositories {
    maven("https://nexus.neetgames.com/repository/maven-public/")
    maven("https://repo.bg-software.com/repository/api/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")
    githubCompileOnly("Slimefun5:InfinityExpansion:v1.1.2")
    compileOnly("com.bgsoftware:WildChestsAPI:2024.1")
}
