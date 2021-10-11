package ru.myacademyhomework.tinkoffmessenger.MessageView

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import ru.myacademyhomework.tinkoffmessenger.R

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var totalWidth = 0
        var totalHeight = 0
        var rowWidth = 0



        for(i in 0 until childCount){
            val child = getChildAt(i)

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight)
            totalHeight = maxOf(totalHeight, child.measuredHeight)
            val childWidth = child.measuredWidth
            val specWidth = MeasureSpec.getSize(widthMeasureSpec)
            rowWidth += childWidth
            if(rowWidth > specWidth){
                totalWidth = maxOf(totalWidth, rowWidth - childWidth)
                rowWidth = childWidth
                totalHeight += child.measuredHeight
            }
        }

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentBottom = 0
        var currentLeft = 0

        for(i in 0 until childCount){
            val child = getChildAt(i)
            if(currentLeft + child.measuredWidth > r){
                currentLeft = 0
                currentBottom += child.measuredHeight
            }
            child.layout(
                currentLeft ,
                currentBottom,
                currentLeft + child.measuredWidth,
                currentBottom + child.measuredHeight
            )
            currentLeft = child.right
        }
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