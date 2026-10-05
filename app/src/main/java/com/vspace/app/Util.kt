package com.vspace.app

import android.content.Context

object Util {
    fun dp(ctx: Context, v: Int) = (v * ctx.resources.displayMetrics.density).toInt()

    fun label(ctx: Context, pkg: String): String = runCatching {
        val pm = ctx.packageManager
        pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString()
    }.getOrDefault(pkg)
}

object LocationStore {
    private fun sp(ctx: Context) = ctx.getSharedPreferences("locations", Context.MODE_PRIVATE)

    fun get(ctx: Context, pkg: String): Pair<Double, Double>? =
        sp(ctx).getString(pkg, null)?.split(",")?.let {
            if (it.size == 2) Pair(it[0].toDouble(), it[1].toDouble()) else null
        }

    fun set(ctx: Context, pkg: String, lat: Double, lng: Double) {
        sp(ctx).edit().putString(pkg, "$lat,$lng").apply()
    }

    fun clear(ctx: Context, pkg: String) {
        sp(ctx).edit().remove(pkg).apply()
    }
}
