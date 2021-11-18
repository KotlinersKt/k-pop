plugins {
    id("com.android.application")
    id("kotlin-android")

    id("com.google.devtools.ksp") version "1.5.31-1.0.0"
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.kotlinerskt.exampleapp"
        minSdk = 21
        targetSdk = 31

        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }

        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-beta02"
    }

    lint {
        baseline = null
    }
}

ksp {
    arg("strict", "false")
    arg("gen_doc", "true")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:1.1.0-beta02")
    implementation("androidx.compose.material3:material3:1.0.0-alpha01")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.0-beta02")
    implementation("androidx.activity:activity-compose:1.4.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.1.0-beta02")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.0-beta02")

    ksp(project(":k-pop"))

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.0-beta02")
}
