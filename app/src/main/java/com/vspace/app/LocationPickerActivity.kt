package com.vspace.app

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class LocationPickerActivity : Activity() {
    private lateinit var map: MapView
    private lateinit var info: TextView
    private lateinit var save: Button
    private var marker: Marker? = null
    private var point: GeoPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pkg = intent.getStringExtra("pkg") ?: return finish()
        title = "الموقع: " + Util.label(this, pkg)

        map = MapView(this).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(3.0)
            controller.setCenter(GeoPoint(25.0, 45.0))
        }
        info = TextView(this).apply {
            text = "المس الخريطة لتحديد نقطة"
            gravity = Gravity.CENTER
            val p = Util.dp(this@LocationPickerActivity, 10)
            setPadding(p, p, p, p)
        }
        save = Button(this).apply {
            text = "حفظ وتطبيق"
            isEnabled = false
            setOnClickListener {
                val g = point ?: return@setOnClickListener
                LocationStore.set(this@LocationPickerActivity, pkg, g.latitude, g.longitude)
                val ok = runCatching { Engine.setLocation(pkg, g.latitude, g.longitude) }.isSuccess
                Toast.makeText(
                    this@LocationPickerActivity,
                    if (ok) "تم. أعد تشغيل التطبيق داخل البيئة ليأخذ الموقع" else "فشل تطبيق الموقع",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }

        map.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean { place(p); return true }
            override fun longPressHelper(p: GeoPoint): Boolean = false
        }))

        LocationStore.get(this, pkg)?.let {
            val g = GeoPoint(it.first, it.second)
            map.controller.setZoom(12.0)
            map.controller.setCenter(g)
            place(g)
        }

        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        root.addView(map, LinearLayout.LayoutParams(-1, 0, 1f))
        root.addView(info)
        root.addView(save)
        setContentView(root)
    }

    private fun place(p: GeoPoint) {
        point = p
        marker?.let { map.overlays.remove(it) }
        marker = Marker(map).apply {
            position = p
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        map.overlays.add(marker)
        map.invalidate()
        info.text = "%.5f, %.5f".format(p.latitude, p.longitude)
        save.isEnabled = true
    }

    override fun onResume() { super.onResume(); map.onResume() }
    override fun onPause() { super.onPause(); map.onPause() }
}
