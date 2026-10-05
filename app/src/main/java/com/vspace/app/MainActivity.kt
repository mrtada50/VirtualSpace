package com.vspace.app

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val items = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var empty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Virtual Space"

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val p = Util.dp(this@MainActivity, 12)
            setPadding(p, p, p, p)
        }
        val add = Button(this).apply {
            text = "＋ إضافة تطبيق للبيئة"
            setOnClickListener { startActivity(Intent(this@MainActivity, AppPickerActivity::class.java)) }
        }
        empty = TextView(this).apply {
            text = "لا توجد تطبيقات داخل البيئة بعد"
            gravity = android.view.Gravity.CENTER
            setPadding(0, Util.dp(this@MainActivity, 24), 0, 0)
        }
        val list = ListView(this)
        adapter = object : ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_2, android.R.id.text1, items
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                val pkg = items[position]
                v.findViewById<TextView>(android.R.id.text1).text = Util.label(context, pkg)
                v.findViewById<TextView>(android.R.id.text2).text =
                    LocationStore.get(context, pkg)
                        ?.let { "الموقع الوهمي: %.5f, %.5f".format(it.first, it.second) }
                        ?: "الموقع الحقيقي"
                return v
            }
        }
        list.adapter = adapter
        list.setOnItemClickListener { _, _, pos, _ -> showActions(items[pos]) }

        root.addView(add)
        root.addView(empty)
        root.addView(list, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        items.clear()
        items.addAll(Engine.installed())
        adapter.notifyDataSetChanged()
        empty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showActions(pkg: String) {
        AlertDialog.Builder(this)
            .setTitle(Util.label(this, pkg))
            .setItems(
                arrayOf("تشغيل", "تغيير الموقع (خريطة)", "إيقاف الموقع الوهمي", "حذف من البيئة")
            ) { _, which ->
                when (which) {
                    0 -> if (!Engine.launch(pkg)) toast("تعذر التشغيل")
                    1 -> startActivity(
                        Intent(this, LocationPickerActivity::class.java).putExtra("pkg", pkg)
                    )
                    2 -> {
                        runCatching { Engine.clearLocation(pkg) }
                        LocationStore.clear(this, pkg)
                        adapter.notifyDataSetChanged()
                        toast("تم إيقاف الموقع الوهمي، أعد تشغيل التطبيق")
                    }
                    3 -> {
                        Engine.uninstall(pkg)
                        LocationStore.clear(this, pkg)
                        onResume()
                    }
                }
            }.show()
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}
