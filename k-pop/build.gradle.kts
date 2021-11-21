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

    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")

    implementation("com.squareup:kotlinpoet:1.10.2")
    implementation("com.squareup:kotlinpoet-ksp:1.10.2")
}
