import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "0.4.15"
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
    version = "191.8026.42"
    type = "IC"
    setPlugins("android")

    //Android Studio 3.5.3
    //Build #AI-191.8026.42.35.6010548, built on November 15, 2019
    //JRE: 1.8.0_202-release-1483-b03 amd64
    //JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
    //Windows 10 10.0
}

tasks.getByName<org.jetbrains.intellij.tasks.RunIdeTask>("runIde") {

    ideDirectory("C:\\Program Files\\Android\\Android Studio")

}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes(
        """
      Add change notes here.<br>
      <em>most HTML tags may be used</em>"""
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
