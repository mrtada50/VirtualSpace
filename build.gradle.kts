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
