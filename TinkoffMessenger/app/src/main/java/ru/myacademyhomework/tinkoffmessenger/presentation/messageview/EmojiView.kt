package ru.myacademyhomework.tinkoffmessenger.presentation.messageview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.myacademyhomework.tinkoffmessenger.R

class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
    }
    var textCount = ""
        set(value) {
            field = value
            text = "$smile $value"
            requestLayout()
        }
    var smile = ""
        set(value) {
            field = value
            text = "$value $textCount"
            requestLayout()
        }
    private var text = ""
    private val textBounds = Rect()
    private val textCoordinate = PointF()

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.EmojiView, defStyleAttr, defStyleAttr)

        smile = typedArray.getString(R.styleable.EmojiView_emojiSmile) ?: "\uD83D\uDE04"
        textCount = typedArray.getString(R.styleable.EmojiView_emojiTextCount) ?: "1"
        text = "$smile $textCount"



        textPaint.color = typedArray.getColor(R.styleable.EmojiView_emojiTextColor, Color.BLACK)
        textPaint.textSize = typedArray.getDimension(R.styleable.EmojiView_emojiTextSize, 50f)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(text, 0, text.length, textBounds)

        val textHeight = textBounds.height()
        val textWidth = textBounds.width()

        val totalWidth = textWidth + paddingRight + paddingLeft
        val totalHeight = textHeight + paddingTop + paddingBottom

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textCoordinate.x = w / 2f
        textCoordinate.y = h / 2f + (paddingTop / 2f)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textCoordinate.x, textCoordinate.y, textPaint)
    }

}