package com.example.nihongomaster.ui.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

class TrendChartView @JvmOverloads constructor(
    c: Context, a: AttributeSet? = null
) : View(c, a) {

    var series: List<List<Float>> = emptyList() // nhiều đường
        set(value) { field = value; invalidate() }

    private val lines = listOf(Paint(Paint.ANTI_ALIAS_FLAG), Paint(Paint.ANTI_ALIAS_FLAG), Paint(Paint.ANTI_ALIAS_FLAG))
    private val grid = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0x22000000; strokeWidth = 1f }

    init {
        // Không set màu cụ thể -> dùng mặc định; nếu muốn: set trong code fragment
        lines.forEach { p -> p.style = Paint.Style.STROKE; p.strokeWidth = 6f }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        // lưới
        repeat(4) { i ->
            val y = h * (i+1) / 5f
            canvas.drawLine(0f, y, w, y, grid)
        }
        if (series.isEmpty()) return
        val maxVal = max(1f, series.flatten().maxOrNull() ?: 1f)
        val count  = series.maxOf { it.size }
        val stepX  = if (count <= 1) w else w / (count - 1)

        series.forEachIndexed { idx, s ->
            val p = lines[idx % lines.size]
            var prevX = 0f; var prevY = h - (s.firstOrNull() ?: 0f) / maxVal * h
            s.forEachIndexed { i, v ->
                val x = i * stepX
                val y = h - (v / maxVal) * h
                if (i > 0) canvas.drawLine(prevX, prevY, x, y, p)
                prevX = x; prevY = y
            }
        }
    }
}
