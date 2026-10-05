plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}

// قيم يقرأها Bcore من rootProject.ext
extra["compileSdkVersion"] = 34
extra["minSdkVersion"] = 21
extra["targetSdkVersion"] = 28
extra["buildToolsVersion"] = "34.0.0"
extra["versionCode"] = 1
extra["versionName"] = "1.0"
extra["javaVersion"] = JavaVersion.VERSION_17

// أسماء إضافية يقرأها Bcore
extra["minSdk"] = 21
extra["targetSdk"] = 28
extra["compileSdk"] = 34
extra["ndkVersion"] = "29.0.13846066"
extra["kotlin_version"] = "1.9.24"
extra["java_version"] = JavaVersion.VERSION_17
