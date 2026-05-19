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
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://nexus.neetgames.com/repository/maven-public/")
    maven("https://repo.bg-software.com/repository/api/")
    maven("https://sefiraat.jfrog.io/artifactory/default-maven-local")
}

dependencies {
    githubImplementation("Slimefun5:SlimefunMetrics:master")
    compileOnly("io.papermc.paper:paper-api:${property("paperApiVersion")}")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    "githubCompileOnly"("Slimefun5:Slimefun5:v5.1.1")

    compileOnly("dev.sefiraat:SefiLib:0.2.6")

    githubCompileOnly("Slimefun5:InfinityExpansion:v1.1.1")
    compileOnly("com.github.Sefiraat:Netheopoiesis:8d1af6c570")
    compileOnly("com.github.schntgaispock:SlimeHUD:1.2.7")
    compileOnly("com.bgsoftware:WildChestsAPI:2024.1")
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.2.017") {
        isTransitive = false
    }

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
}
