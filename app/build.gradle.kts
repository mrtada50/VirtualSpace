plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vspace.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vspace.app"
        minSdk = 21
        targetSdk = 28 // توصية BlackBox لتوافق أفضل
        versionCode = 1
        versionName = "1.0"
        ndk { abiFilters += listOf("arm64-v8a", "armeabi-v7a") }
    }

    buildTypes {
        release { isMinifyEnabled = false }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    packaging { jniLibs { useLegacyPackaging = true } }
}

dependencies {
    if (rootProject.file("Bcore").exists()) implementation(project(":Bcore"))
    implementation("org.osmdroid:osmdroid-android:6.1.18")
}
