import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"

    id("com.android.application") version "7.2.0-alpha04" apply false
}

tasks {
    withType<KotlinCompile> {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }
}

allprojects {
    tasks {
        withType<KotlinCompile> {
            sourceCompatibility = JavaVersion.VERSION_11.toString()
            targetCompatibility = JavaVersion.VERSION_11.toString()
        }
    }
}
