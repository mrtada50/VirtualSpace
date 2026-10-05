pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories { google(); mavenCentral(); maven("https://jitpack.io") }
}
rootProject.name = "VirtualSpace"
include(":app")

// موديولات المحرك تُنسخ بواسطة GitHub Actions، وتُضمَّن هنا تلقائياً
val skip = setOf("app", "engine-src", "buildSrc", "build")
rootDir.listFiles()
    ?.filter {
        it.isDirectory && !it.name.startsWith(".") && it.name !in skip &&
            (File(it, "build.gradle").exists() || File(it, "build.gradle.kts").exists())
    }
    ?.forEach { include(":${it.name}") }
