package gitlin.kothub.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import gitlin.kothub.R

class CircleView(context: Context, val attrs: AttributeSet?) : View(context, attrs) {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): this(context, attrs)
    constructor(context: Context): this(context, null)

    var color: Int = android.R.color.black
    var radius = 10

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleView, 0, 0)

        if (a.hasValue(R.styleable.CircleView_color)) {
            this.color = a.getColor(R.styleable.CircleView_color, resources.getColor(android.R.color.black))
        }

        if (a.hasValue(R.styleable.CircleView_radius)) {
            radius = a.getDimensionPixelSize(R.styleable.CircleView_radius, radius)
        }

        a.recycle()
    }

    fun getFill(): Paint {
        val fillPaint = Paint()
        fillPaint.flags = Paint.ANTI_ALIAS_FLAG
        fillPaint.style = Paint.Style.FILL
        fillPaint.color = color
        return fillPaint
    }



    override fun onDraw(canvas: Canvas) {
        val ox = width / 2f
        val oy = height / 2f
        canvas.drawCircle(ox, oy, radius.toFloat(), getFill())
    }
}