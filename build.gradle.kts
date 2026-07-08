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
<<<<<<< HEAD
<<<<<<< HEAD
    implementation("com.github.Slimefun5:SlimefunMetrics:master-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:${property("paperApiVersion")}")
=======
    githubCompileOnly("Slimefun5:Slimefun5:gh-v5.2.3.2")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
>>>>>>> origin/experimental
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
=======
>>>>>>> origin/experimental
    compileOnly("org.jetbrains:annotations:23.0.0")
    githubCompileOnly("Slimefun5:InfinityExpansion:v1.1.2")
    compileOnly("com.bgsoftware:WildChestsAPI:2024.1")
<<<<<<< HEAD
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.2.017") {
        isTransitive = false
    }

    }
=======
}
<<<<<<< HEAD
>>>>>>> origin/experimental

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to displayVersion)
        }
    }
    jar {
        enabled = false
    }
    shadowJar {
<<<<<<< HEAD
<<<<<<< HEAD
        archiveFileName.set("Networks v${project.version}.jar")
                exclude("META-INF/**")
=======
        archiveFileName.set("Networks-1.0.0-UNOFFICIAL.jar")
=======
        archiveFileName.set("Networks-$displayVersion.jar")
>>>>>>> origin/experimental
        exclude("META-INF/**")
>>>>>>> origin/experimental
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
=======
>>>>>>> origin/experimental
