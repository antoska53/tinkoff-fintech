package ru.myacademyhomework.tinkoffmessenger.messageview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import ru.myacademyhomework.tinkoffmessenger.R

class NameMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    init {
        inflate(context, R.layout.name_message_layout, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var totalWidth = 0
        var totalHeight = 0


        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)
            totalWidth = maxOf(totalWidth, child.measuredWidth)
            totalHeight += child.measuredHeight
        }

        val resultWidth = resolveSize(totalWidth + paddingRight + paddingLeft, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val textViewName = getChildAt(0)
        val textViewMessage = getChildAt(1)

        textViewName.layout(
            paddingLeft,
            paddingTop,
            textViewName.measuredWidth + paddingLeft,
            textViewName.measuredHeight + paddingTop
        )

        textViewMessage.layout(
            paddingLeft,
            textViewName.bottom,
            textViewMessage.measuredWidth + paddingLeft,
            textViewName.bottom + textViewMessage.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

}