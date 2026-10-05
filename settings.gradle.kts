pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories { google(); mavenCentral(); maven("https://jitpack.io") }
}
rootProject.name = "VirtualSpace"
include(":app")
// محرك BlackBox يُنسخ تلقائياً بواسطة GitHub Actions (أو setup-engine.sh)
if (file("Bcore").exists()) include(":Bcore")
