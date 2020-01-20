import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "0.4.16"
    kotlin("jvm") version "1.3.21"
}

group = "com.fnovellon"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("javazoom:jlayer:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2018.3"
}


tasks {
    withType<PublishTask> {
        token(project.findProperty("pluginToken") as String?)
        channels("beta")
    }
    withType<RunIdeTask> {
        ideDirectory("C:\\Program Files\\Android\\Android Studio")
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
