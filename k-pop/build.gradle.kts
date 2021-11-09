plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    
    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.0")
}
