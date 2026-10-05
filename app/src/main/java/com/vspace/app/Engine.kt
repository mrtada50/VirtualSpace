package com.vspace.app

import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.entity.location.BLocation
import top.niunaijun.blackbox.fake.frameworks.BLocationManager

/**
 * كل التعامل مع محرك BlackBox في هذا الملف فقط.
 * إذا اختلفت أسماء الدوال في نسخة المحرك، يتعدل هنا فقط.
 */
object Engine {
    private const val USER = 0

    fun installed(): List<String> = runCatching {
        BlackBoxCore.get().getInstalledApplications(0, USER).map { it.packageName }
    }.getOrDefault(emptyList())

    fun install(pkg: String): Boolean = runCatching {
        BlackBoxCore.get().installPackageAsUser(pkg, USER).success
    }.getOrDefault(false)

    fun launch(pkg: String): Boolean = runCatching {
        BlackBoxCore.get().launchApk(pkg, USER)
    }.getOrDefault(false)

    fun uninstall(pkg: String) {
        runCatching { BlackBoxCore.get().uninstallPackageAsUser(pkg, USER) }
    }

    fun setLocation(pkg: String, lat: Double, lng: Double) {
        val m = BLocationManager.get()
        m.setPattern(USER, pkg, BLocationManager.OWN_MODE)
        m.setLocation(USER, pkg, BLocation(lat, lng))
    }

    fun clearLocation(pkg: String) {
        BLocationManager.get().setPattern(USER, pkg, BLocationManager.CLOSE_MODE)
    }
}
