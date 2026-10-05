package com.vspace.app

import android.app.Activity
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlin.concurrent.thread

class AppPickerActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "اختر تطبيقاً"
        val pm = packageManager
        val apps = pm.getInstalledApplications(0)
            .filter {
                val sys = (it.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                val updated = (it.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
                (!sys || updated) && it.packageName != packageName
            }
            .sortedBy { pm.getApplicationLabel(it).toString().lowercase() }

        val lv = ListView(this)
        lv.adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            apps.map { pm.getApplicationLabel(it).toString() + "\n" + it.packageName }
        )
        lv.setOnItemClickListener { _, _, pos, _ ->
            val pkg = apps[pos].packageName
            Toast.makeText(this, "جارٍ التثبيت داخل البيئة...", Toast.LENGTH_SHORT).show()
            thread {
                val ok = Engine.install(pkg)
                runOnUiThread {
                    Toast.makeText(this, if (ok) "تم التثبيت" else "فشل التثبيت", Toast.LENGTH_LONG).show()
                    if (ok) finish()
                }
            }
        }
        setContentView(lv)
    }
}
