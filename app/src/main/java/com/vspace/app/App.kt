package com.vspace.app

import android.app.Application
import android.content.Context
import org.osmdroid.config.Configuration
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.app.configuration.ClientConfiguration
import java.io.File

class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        BlackBoxCore.get().doAttachBaseContext(base, object : ClientConfiguration() {
            override fun getHostPackageName(): String = base.packageName
        })
    }

    override fun onCreate() {
        super.onCreate()
        BlackBoxCore.get().doCreate()
        Configuration.getInstance().apply {
            userAgentValue = packageName
            osmdroidBasePath = File(cacheDir, "osm")
            osmdroidTileCache = File(cacheDir, "osm/tiles")
        }
    }
}
