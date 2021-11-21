import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

tasks {
    withType<KotlinCompile> {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.0-1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
}
