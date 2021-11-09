plugins {
    id("com.google.devtools.ksp") version "1.5.31-1.0.0"
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    ksp(project(":k-pop"))
}
