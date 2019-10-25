package ftc.goldengears.ggconfig

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View

class FieldView : View {
    private var paint = Paint()
    private var image : Drawable? = null
    private lateinit var canvas : Canvas
    private val FIELD_INCHES = 144
    private val PANEL = 24f // field is 6x6
    private val ROBOT = 1/8f // robot 18in / field 144in

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 4f
    }

    constructor(context: Context) : super(context) {
        image = ContextCompat.getDrawable(context, R.drawable.skystone_field)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        image = ContextCompat.getDrawable(context, R.drawable.skystone_field)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        image = ContextCompat.getDrawable(context, R.drawable.skystone_field)
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    public override fun onDraw(canvas: Canvas) {
        this.canvas = canvas

        image?.bounds = canvas.clipBounds
        image?.draw(canvas)

        drawRobot(0f, 0f)
        drawRobot(-72f, -72f)
        drawRobot(72f, 72f)

//        invalidate() // causes onDraw to be called again
    }

    // TODO: have rotation
    // example:
//    Matrix matrix = new Matrix();
//    matrix.setRotate(angle, imageCenterX, imageCenterY);
//    yourCanvas.drawBitmap(yourBitmap, matrix, null);
    fun drawRobot(xf: Float, yf: Float) {
        var hr = ROBOT * width / 2
        var xc = toCanvas(xf)
        var yc = toCanvas(yf)

        canvas.drawLines(floatArrayOf(
                xc - hr, yc - hr,
                xc - hr, yc + hr,

                xc - hr, yc + hr,
                xc + hr, yc + hr,

                xc + hr, yc + hr,
                xc + hr, yc - hr,

                xc + hr, yc - hr,
                xc - hr, yc - hr), paint)

        canvas.drawCircle(xc, yc, 10f, paint)
    }

    fun toCanvas(x: Float): Float {
        return (width / 2) + (width / FIELD_INCHES) * x
    }

    fun toField(x: Float): Float {
        return x
    }

}